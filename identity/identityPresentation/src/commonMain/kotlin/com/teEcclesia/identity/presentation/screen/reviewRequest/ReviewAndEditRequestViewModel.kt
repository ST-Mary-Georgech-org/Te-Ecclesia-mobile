package com.teEcclesia.identity.presentation.screen.reviewRequest

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.ApproveUserRequest
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.identity.presentation.util.toUiText
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.validation.getNationalIdValidationError
import com.teEcclesia.shared.domain.utils.validation.isValidApartmentInput
import com.teEcclesia.shared.domain.utils.validation.isValidBuildingNoInput
import com.teEcclesia.shared.domain.utils.validation.isValidCodeFormat
import com.teEcclesia.shared.domain.utils.validation.isValidCodeInput
import com.teEcclesia.shared.domain.utils.validation.isValidEmailInput
import com.teEcclesia.shared.domain.utils.validation.isValidFinalEmail
import com.teEcclesia.shared.domain.utils.validation.isValidFloorInput
import com.teEcclesia.shared.domain.utils.validation.isValidNationalIdInput
import com.teEcclesia.shared.domain.utils.validation.isValidPhoneInput
import com.teEcclesia.shared.domain.utils.validation.validateArabicName
import com.teEcclesia.shared.domain.utils.validation.validatePhone
import com.teEcclesia.shared.domain.utils.validation.getPasswordValidationError
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitCameraType
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openCameraPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_approve_request
import teecclesia.designsystem.generated.resources.failed_to_load_areas
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import teecclesia.designsystem.generated.resources.failed_to_load_priests
import teecclesia.designsystem.generated.resources.failed_to_load_ranks
import teecclesia.designsystem.generated.resources.failed_to_load_request
import teecclesia.designsystem.generated.resources.failed_to_reject_request
import teecclesia.designsystem.generated.resources.failed_to_search_child
import teecclesia.designsystem.generated.resources.failed_to_search_partner
import teecclesia.designsystem.generated.resources.field_required
import teecclesia.designsystem.generated.resources.invalid_arabic_name
import teecclesia.designsystem.generated.resources.invalid_code_format
import teecclesia.designsystem.generated.resources.invalid_email_format
import teecclesia.designsystem.generated.resources.invalid_home_phone_format
import teecclesia.designsystem.generated.resources.invalid_phone_format
import teecclesia.designsystem.generated.resources.invalid_year_format
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.user_created_successfully

import com.teEcclesia.identity.domain.service.AuthorizationService
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class ReviewAndEditRequestViewModel(
    private val userId: String? = null,
    private val isFromSearch: Boolean = false,
    private val profileRepository: ProfileRepository,
    private val registerRepository: RegisterRepository,
    private val lookupRepository: LookupRepository,
    private val authorizationService: AuthorizationService
) : BaseViewModel<ReviewAndEditRequestUiState>(ReviewAndEditRequestUiState(userId = userId ?: "", isReadOnlyMode = isFromSearch)),
    ReviewAndEditRequestInteractionListener {

    private val priestsPaginator = createPaginator(
        loadPage = { page ->
            registerRepository.getConfessionPriests(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    confessionPriests = current.confessionPriests + items.data
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { it.copy(isPriestLoading = loading) }
        },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_priests),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    )

    private var callerRole: UserRole? = null
    private var callerCanApproveRequests: Boolean = false
    private var callerResponsibleStageIds: List<Long> = emptyList()
    private var callerResponsibleYearIds: List<Long> = emptyList()
    private var rawEducationalStages: List<LookupResponse> = emptyList()

    private val stagesPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getEducationalStages(
                pageQuery = PageQuery(page = page, size = 20),
                forRole = state.value.selectedRole
            ).toPagedData()
        },
        onSuccess = { items ->
            rawEducationalStages = rawEducationalStages + items.data
            updateState { it.copy(isStageEndReached = items.isLastPage) }
            filterAndApplyEducationalStages()
        },
        onLoadUpdated = { loading ->
            updateState { it.copy(isStageLoading = loading) }
        },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_educational_stages),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    )

    private val ranksPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getRanks(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    ranks = current.ranks + items.data
                )
            }
        },
        onLoadUpdated = { _ -> },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_ranks),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    )

    private fun updateDerivedProperties(current: ReviewAndEditRequestUiState): ReviewAndEditRequestUiState {
        val allAvailableYears = current.educationalStages.flatMap { stage ->
            stage.subItems.map { year ->
                year.copy(name = "${stage.name}, ${year.name}")
            }
        }.distinctBy { it.id }

        val fullServantStage = current.educationalStages.find { it.id == current.servantEducationalStage?.id } ?: current.servantEducationalStage
        val servantAvailableYears = fullServantStage?.subItems ?: emptyList()

        val fullStudentStage = current.educationalStages.find { it.id == current.studentEducationalStage?.id } ?: current.studentEducationalStage
        
        var canEdit = false
        if (current.userId.isBlank()) {
            canEdit = true
        } else if (callerRole == UserRole.ADMIN) {
            canEdit = true
        } else if (callerRole == UserRole.KHADEM) {
            val targetRole = current.selectedRole
            if (targetRole == UserRole.MAKHDOOM) {
                val targetStageId = current.studentEducationalStage?.id
                val targetYearId = current.studentEducationalYear?.id
                val isRespForStageOrYear = (targetStageId != null && callerResponsibleStageIds.contains(targetStageId)) ||
                        (targetYearId != null && callerResponsibleYearIds.contains(targetYearId))

                if (current.isUpdateMode) {
                    canEdit = isRespForStageOrYear
                } else {
                    canEdit = callerCanApproveRequests && isRespForStageOrYear
                }
            } else {
                canEdit = false
            }
        }

        return current.copy(
            allAvailableYearsForPermissions = allAvailableYears,
            servantAvailableYears = servantAvailableYears,
            studentEducationalStage = fullStudentStage,
            servantEducationalStage = fullServantStage,
            canEditUser = canEdit
        )
    }

    private fun filterAndApplyEducationalStages() {
        val targetRole = state.value.selectedRole
        val stagesForApplicant = if (targetRole == UserRole.MAKHDOOM) {
            rawEducationalStages.filter { !it.isKhademOnly }
        } else {
            rawEducationalStages
        }

        val filtered = if (callerRole == UserRole.KHADEM) {
            stagesForApplicant.mapNotNull { stage ->
                val isStageResp = callerResponsibleStageIds.contains(stage.id)
                val respYears = stage.subItems.filter { year ->
                    callerResponsibleYearIds.contains(year.id)
                }

                if (isStageResp) {
                    stage
                } else if (respYears.isNotEmpty()) {
                    stage.copy(subItems = respYears)
                } else null
            }
        } else {
            stagesForApplicant
        }

        updateState { current ->
            updateDerivedProperties(current.copy(educationalStages = filtered))
        }
    }

    private fun loadCallerProfile() {
        launch {
            callerRole = authorizationService.getUserRole()
            callerCanApproveRequests = authorizationService.canApproveRequests()
            callerResponsibleStageIds = authorizationService.getResponsibleStageIds()
            callerResponsibleYearIds = authorizationService.getResponsibleYearIds()
            filterAndApplyEducationalStages()
            delay(250.milliseconds)
            updateState { 
                it.copy(
                    isLoading = false,
                    isRoleEditable = if (it.isUpdateMode) (callerRole == UserRole.ADMIN) else it.isRoleEditable
                ) 
            }
        }
    }

    init {
        updateState { it.copy(isUpdateMode = isFromSearch && userId.isNotBlank()) }
        
        if (!userId.isNullOrBlank()) {
            loadRequestDetails()
        } else {
            updateState {
                it.copy(
                    isLoading = true,
                    selectedRole = UserRole.MAKHDOOM,
                    isRoleEditable = false
                )
            }
            loadCallerProfile()
        }
        loadCallerProfile()
        loadConfessionPriests()
        loadEducationalStages()
        loadRanks()
    }

    private fun loadRequestDetails() {
        val uid = userId ?: return
        updateState { it.copy(isLoading = !it.isRefreshing) }
        tryToCall(
            block = { profileRepository.getUserProfile(uid) },
            onSuccess = { profile ->
                updateState {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        userProfile = profile,
                        code = profile.code,
                        imageUrl = profile.imageUrl,
                        firstName = profile.firstName,
                        secondName = profile.secondName,
                        thirdName = profile.thirdName,
                        lastName = profile.lastName,
                        displayName = profile.displayName,
                        nationalId = profile.nationalId,
                        job = profile.job,
                        isFromAnotherChurch = profile.confessionPriest == null && profile.externalConfessionPriestName.isNotBlank(),
                        confessionPriestId = profile.confessionPriest?.id,
                        confessionPriestName = profile.confessionPriest?.name ?: profile.externalConfessionPriestName,
                        confessionPriestChurch = profile.externalConfessionChurch,
                        confessionPriestPhone = profile.externalConfessionPhone,
                        phone = profile.phone.removePrefix("+2"),
                        homePhone = profile.homePhone.removePrefix("02"),
                        email = profile.email,
                        buildingNo = profile.buildingNo,
                        street = profile.street,
                        streetBranch = profile.streetBranch,
                        area = profile.area,
                        floor = profile.floor,
                        apartment = profile.apartment,
                        specialMark = profile.specialMark,
                        selectedRole = profile.role,
                        submittedAt = profile.createdAt.toString().replace("T", " ").take(16),

                        servantEducationalStage = profile.khademProfile?.educationalStage,
                        servantEducationalYear = profile.khademProfile?.educationalYear,
                        canApproveNewRequests = profile.khademProfile?.canApproveRequests ?: false,
                        responsibleStages = profile.khademProfile?.responsibleStages ?: emptyList(),
                        responsibleYears = profile.khademProfile?.responsibleYears ?: emptyList(),
                        isStageEndReached = false,

                        selectedPartner = profile.parentProfile?.partner?.let { p ->
                            UserSummary(id = p.id, name = p.name, imageUrl = p.imageUrl, code = p.code ?: "")
                        },
                        selectedChildren = profile.parentProfile?.children?.map { c ->
                            UserSummary(id = c.id, name = c.name, imageUrl = c.imageUrl, code = c.code ?: "")
                        } ?: emptyList(),

                        kahenEducationalStages = profile.kahenProfile?.educationalStages ?: emptyList(),

                        studentEducationalStage = profile.makhdoomProfile?.educationalStage,
                        studentEducationalYear = profile.makhdoomProfile?.educationalYear,
                        shamamsaStatus = profile.makhdoomProfile?.shamamsaStudyStatus ?: ShamamsaStudyStatus.NO,
                        fatherPhone = profile.makhdoomProfile?.fatherPhone ?: "",
                        fatherWhatsapp = profile.makhdoomProfile?.fatherWhatsapp ?: "",
                        motherPhone = profile.makhdoomProfile?.motherPhone ?: "",
                        motherWhatsapp = profile.makhdoomProfile?.motherWhatsapp ?: "",
                        isFatherDeceased = profile.makhdoomProfile?.isFatherDeceased ?: false,
                        isMotherDeceased = profile.makhdoomProfile?.isMotherDeceased ?: false,

                        isOrdained = profile.ordinationProfile != null,
                        selectedRank = profile.ordinationProfile?.rank,
                        isOrdainedInThisChurch = !(profile.ordinationProfile?.isOrdinationInAnotherChurch ?: false),
                        ordinationYear = profile.ordinationProfile?.ordinationYear?.toString() ?: "",
                        bishopName = profile.ordinationProfile?.bishopName ?: "",
                        ordinationPlace = profile.ordinationProfile?.ordinationPlace ?: "",
                        ordinationCertificateFileName = profile.ordinationProfile?.certificateImageUrl,
                        identityCertificateFileName = profile.makhdoomProfile?.identityDocumentImageUrl ?: profile.parentProfile?.nationalIdImageUrl
                    ).let { state -> updateDerivedProperties(state) }
                }
            },
            onError = { throwable ->
                updateState { it.copy(isLoading = false, isRefreshing = false) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_request),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }

    private fun loadConfessionPriests() {
        launch {
            updateState { it.copy(confessionPriests = emptyList()) }
            priestsPaginator.reset()
            priestsPaginator.loadNextItems()
        }
    }

    private fun loadEducationalStages() {
        launch {
            rawEducationalStages = emptyList()
            updateState { it.copy(educationalStages = emptyList()) }
            stagesPaginator.reset()
            stagesPaginator.loadNextItems()
        }
    }

    private fun loadRanks() {
        launch {
            updateState { it.copy(ranks = emptyList()) }
            ranksPaginator.reset()
            ranksPaginator.loadNextItems()
        }
    }

    override fun onLoadNextPriests() {
        launch {
            priestsPaginator.loadNextItems()
        }
    }

    fun validateStep1(): Boolean {
        val s = state.value
        val codeError = if (s.code.isNotBlank() && !isValidCodeFormat(s.code)) {
            UiText.StringRes(Res.string.invalid_code_format)
        } else null

        val firstNameError = if (s.firstName.isBlank()) {
            UiText.StringRes(Res.string.field_required)
        } else if (!validateArabicName(s.firstName)) {
            UiText.StringRes(Res.string.invalid_arabic_name)
        } else null

        val secondNameError = if (s.secondName.isBlank()) {
            UiText.StringRes(Res.string.field_required)
        } else if (!validateArabicName(s.secondName)) {
            UiText.StringRes(Res.string.invalid_arabic_name)
        } else null

        val thirdNameError = if (s.thirdName.isBlank()) {
            UiText.StringRes(Res.string.field_required)
        } else if (!validateArabicName(s.thirdName)) {
            UiText.StringRes(Res.string.invalid_arabic_name)
        } else null

        val lastNameError = if (s.lastName.isBlank()) {
            UiText.StringRes(Res.string.field_required)
        } else if (!validateArabicName(s.lastName)) {
            UiText.StringRes(Res.string.invalid_arabic_name)
        } else null

        val displayNameError = if (s.displayName.isBlank()) {
            UiText.StringRes(Res.string.field_required)
        } else null

        val nationalIdError = getNationalIdValidationError(s.nationalId)?.toUiText()

        val confessionPriestErr =
            if (!s.isFromAnotherChurch && s.selectedConfessionPriest == null && s.confessionPriestName.isBlank()) UiText.StringRes(Res.string.field_required) else null
        val externalNameErr =
            if (s.isFromAnotherChurch && s.confessionPriestName.isBlank()) UiText.StringRes(Res.string.field_required) else null
        val externalChurchErr =
            if (s.isFromAnotherChurch && s.confessionPriestChurch.isBlank()) UiText.StringRes(Res.string.field_required) else null
        val externalPhoneErr =
            if (s.isFromAnotherChurch && !validatePhone(s.confessionPriestPhone)) UiText.StringRes(Res.string.invalid_phone_format) else null

        val phoneErr = if (validatePhone(s.phone)) null else UiText.StringRes(Res.string.invalid_phone_format)
        val homePhoneErr = if (s.homePhone.isBlank() || s.homePhone.length == 8) null else UiText.StringRes(Res.string.invalid_home_phone_format)
        val emailErr = if (s.email.isBlank() || isValidFinalEmail(s.email)) null else UiText.StringRes(Res.string.invalid_email_format)
        val passErr = if (s.isUpdateMode && s.password.isNotBlank()) getPasswordValidationError(s.password)?.toUiText() else null

        val buildingErr = if (s.buildingNo.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val streetErr = if (s.street.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val areaErr = if (s.area.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val floorErr = if (s.floor.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val markErr = if (s.specialMark.isNotBlank()) null else UiText.StringRes(Res.string.field_required)

        val hasError = listOfNotNull(
            codeError,
            firstNameError,
            secondNameError,
            thirdNameError,
            lastNameError,
            displayNameError,
            nationalIdError,
            confessionPriestErr,
            externalNameErr,
            externalChurchErr,
            externalPhoneErr,
            phoneErr,
            homePhoneErr,
            emailErr,
            passErr,
            buildingErr,
            streetErr,
            areaErr,
            floorErr,
            markErr
        ).isNotEmpty()

        updateState {
            it.copy(
                codeError = codeError,
                firstNameError = firstNameError,
                secondNameError = secondNameError,
                thirdNameError = thirdNameError,
                lastNameError = lastNameError,
                displayNameError = displayNameError,
                nationalIdError = nationalIdError,
                confessionPriestError = confessionPriestErr,
                externalPriestNameError = externalNameErr,
                externalPriestChurchError = externalChurchErr,
                externalPriestPhoneError = externalPhoneErr,
                phoneError = phoneErr,
                homePhoneError = homePhoneErr,
                emailError = emailErr,
                passwordError = passErr,
                buildingNoError = buildingErr,
                streetError = streetErr,
                areaError = areaErr,
                floorError = floorErr,
                specialMarkError = markErr
            )
        }

        return !hasError
    }

    fun validateStep2(): Boolean {
        val s = state.value
        return when (s.selectedRole) {
            UserRole.KHADEM -> {
                val stageErr = if (s.servantEducationalStage == null) UiText.StringRes(Res.string.field_required) else null
                val yearErr = if (s.servantEducationalStage?.subItems?.isNotEmpty() == true && s.servantEducationalYear == null) UiText.StringRes(Res.string.field_required) else null
                val hasError = listOfNotNull(stageErr, yearErr).isNotEmpty()
                updateState {
                    it.copy(
                        stageError = stageErr,
                        yearError = yearErr
                    )
                }
                !hasError
            }

            UserRole.MAKHDOOM -> {
                val rankErr = if (s.isOrdained && s.selectedRank == null) UiText.StringRes(Res.string.field_required) else null
                val stageErr = if (s.studentEducationalStage == null) UiText.StringRes(Res.string.field_required) else null
                val yearErr = if (!s.studentEducationalStage?.subItems.isNullOrEmpty() && s.studentEducationalYear == null) {
                    UiText.StringRes(Res.string.field_required)
                } else null

                val ordinationYearErr = if (s.isOrdained) {
                    if (s.ordinationYear.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (s.ordinationYear.length != 4 || !s.ordinationYear.all { it.isDigit() }) {
                        UiText.StringRes(Res.string.invalid_year_format)
                    } else null
                } else null

                val fatherPhoneErr = if (!s.isFatherDeceased && s.fatherPhone.isNotBlank() && !validatePhone(s.fatherPhone)) {
                    UiText.StringRes(Res.string.invalid_phone_format)
                } else null

                val fatherWhatsappErr = if (!s.isFatherDeceased && s.fatherWhatsapp.isNotBlank() && !validatePhone(s.fatherWhatsapp)) {
                    UiText.StringRes(Res.string.invalid_phone_format)
                } else null

                val motherPhoneErr = if (!s.isMotherDeceased && s.motherPhone.isNotBlank() && !validatePhone(s.motherPhone)) {
                    UiText.StringRes(Res.string.invalid_phone_format)
                } else null

                val motherWhatsappErr = if (!s.isMotherDeceased && s.motherWhatsapp.isNotBlank() && !validatePhone(s.motherWhatsapp)) {
                    UiText.StringRes(Res.string.invalid_phone_format)
                } else null

                val hasError = listOfNotNull(
                    rankErr, stageErr, yearErr, ordinationYearErr,
                    fatherPhoneErr, fatherWhatsappErr, motherPhoneErr, motherWhatsappErr
                ).isNotEmpty()

                updateState {
                    it.copy(
                        rankError = rankErr,
                        stageError = stageErr,
                        yearError = yearErr,
                        ordinationYearError = ordinationYearErr,
                        fatherPhoneError = fatherPhoneErr,
                        fatherWhatsappError = fatherWhatsappErr,
                        motherPhoneError = motherPhoneErr,
                        motherWhatsappError = motherWhatsappErr
                    )
                }
                !hasError
            }

            UserRole.KAHEN -> {
                val stageErr = if (s.kahenEducationalStages.isEmpty()) UiText.StringRes(Res.string.field_required) else null
                updateState { it.copy(stagesError = stageErr) }
                stageErr == null
            }

            UserRole.PARENT -> {
                true
            }

            else -> true
        }
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onNextStep() {
        if (state.value.currentStep == 1 && !validateStep1()) return
        if (state.value.currentStep == 2 && !validateStep2()) return
        updateState { current ->
            if (current.currentStep < current.totalSteps) {
                current.copy(currentStep = current.currentStep + 1)
            } else {
                current
            }
        }
    }

    override fun onPreviousStep() {
        if (state.value.currentStep > 1) {
            updateState { current -> current.copy(currentStep = current.currentStep - 1) }
        } else {
            onClickBack()
        }
    }

    override fun onStepClicked(step: Int) {
        updateState { current ->
            if (step in 1..current.totalSteps) {
                current.copy(currentStep = step)
            } else {
                current
            }
        }
    }
    
    override fun onApproveRequest() {
        val isStep1Valid = validateStep1()
        val isStep2Valid = validateStep2()
        if (!isStep1Valid || !isStep2Valid) {
            if (!isStep1Valid) {
                updateState { it.copy(currentStep = 1) }
            }
            return
        }
        updateState { it.copy(isSubmitting = true) }

        val registerRequest = state.value.toRegisterRequest()
        val s = state.value

        if (!userId.isNullOrBlank()) {
            tryToCall(
                block = {
                    val customCode = s.code.ifBlank { null }
                    val request = ApproveUserRequest(
                        customCode = customCode,
                        updateProfileData = registerRequest
                    )
                    
                    if (isFromSearch) {
                        profileRepository.updateUser(userId, request)
                    } else {
                        profileRepository.approveUser(userId, request)
                    }
                },
                onSuccess = {
                    updateState { it.copy(isSubmitting = false) }
                    popBackStack("handledUserId" to userId)
                },
                onError = { throwable ->
                    updateState { it.copy(isSubmitting = false) }
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_approve_request),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                }
            )
        } else {
            tryToCall(
                block = {
                    profileRepository.createMakhdoomDirectly(
                        request = registerRequest,
                        imageBytes = s.imageBytes,
                        identityDocumentBytes = s.identityCertificateBytes
                    )
                },
                onSuccess = {
                    updateState { it.copy(isSubmitting = false) }
                    showSnackBar(
                        title = UiText.StringRes(Res.string.user_created_successfully),
                        message = UiText.StringRes(Res.string.user_created_successfully),
                        isSuccess = true
                    )
                    popBackStack("handledUserId" to userId.orEmpty())
                },
                onError = { throwable ->
                    updateState { it.copy(isSubmitting = false) }
                    showSnackBar(
                        title = UiText.StringRes(Res.string.error_occurred),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                }
            )
        }
    }

    override fun onRejectRequest(reason: String) {
        val uid = userId ?: return
        if (reason.isBlank()) {
            updateState { it.copy(notesError = UiText.StringRes(Res.string.field_required)) }
            return
        }
        updateState { it.copy(isSubmitting = true, isRejectDialogVisible = false, notesError = null) }
        tryToCall(
            block = { profileRepository.rejectUser(uid, reason.trim()) },
            onSuccess = {
                updateState { it.copy(isSubmitting = false) }
                popBackStack("handledUserId" to uid)
            },
            onError = { throwable ->
                updateState { it.copy(isSubmitting = false) }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_reject_request),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }

    override fun onToggleRejectDialog(isVisible: Boolean) {
        updateState { it.copy(isRejectDialogVisible = isVisible) }
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        if (!userId.isNullOrBlank()) {
            loadRequestDetails()
            loadConfessionPriests()
            loadEducationalStages()
            loadRanks()
        } else {
            loadConfessionPriests()
            loadEducationalStages()
            loadRanks()
            launch {
                delay(300.milliseconds)
                updateState { it.copy(isRefreshing = false) }
            }
        }
    }

    override fun onClickUpload(target: UploadTarget) {
        updateState { copy(isUploadBottomSheetVisible = true, activeUploadTarget = target) }
    }

    override fun onDismissUploadBottomSheet() {
        updateState { copy(isUploadBottomSheetVisible = false, activeUploadTarget = null) }
    }

    override fun onFileOptionPicked(option: FilePickOption) {
        val target = state.value.activeUploadTarget ?: return
        launch {
            val file = when (option) {
                FilePickOption.CAMERA -> FileKit.openCameraPicker(type = FileKitCameraType.Photo)
                FilePickOption.GALLERY -> FileKit.openFilePicker(
                    type = FileKitType.Image,
                    mode = FileKitMode.Single
                )

                FilePickOption.FILES -> FileKit.openFilePicker(
                    type = FileKitType.File(
                        extensions = listOf("pdf", "png", "jpg", "jpeg", "webp")
                    ),
                    mode = FileKitMode.Single
                )
            }
            if (file == null) return@launch
            val bytes = file.readBytes()
            val fileName = file.name
            onSelectImageBytes(target, bytes, fileName)
        }
    }

    override fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?) {
        when (target) {
            UploadTarget.PROFILE_PHOTO -> updateState { copy(imageBytes = bytes) }
            UploadTarget.ORDINATION_CERTIFICATE -> updateState {
                copy(
                    ordinationCertificateBytes = bytes,
                    ordinationCertificateFileName = fileName
                )
            }

            UploadTarget.IDENTITY_CERTIFICATE -> updateState {
                copy(
                    identityCertificateBytes = bytes,
                    identityCertificateFileName = fileName
                )
            }
        }
    }

    override fun onCodeChanged(value: String) {
        val trimmed = value.trim()
        if (isValidCodeInput(trimmed)) {
            updateState { it.copy(code = trimmed, codeError = null) }
        }
    }

    override fun onFirstNameChanged(value: String) {
        if (value.isEmpty() || validateArabicName(value)) {
            updateState { it.copy(firstName = value, firstNameError = null) }
        }
    }

    override fun onSecondNameChanged(value: String) {
        if (value.isEmpty() || validateArabicName(value)) {
            updateState { it.copy(secondName = value, secondNameError = null) }
        }
    }

    override fun onThirdNameChanged(value: String) {
        if (value.isEmpty() || validateArabicName(value)) {
            updateState { it.copy(thirdName = value, thirdNameError = null) }
        }
    }

    override fun onLastNameChanged(value: String) {
        if (value.isEmpty() || validateArabicName(value)) {
            updateState { it.copy(lastName = value, lastNameError = null) }
        }
    }

    override fun onDisplayNameChanged(value: String) = updateState { it.copy(displayName = value, displayNameError = null) }

    override fun onNationalIdChanged(value: String) {
        val trimmed = value.trim()
        if (trimmed.isEmpty() || isValidNationalIdInput(trimmed)) {
            val error = if (trimmed.length == 14) {
                getNationalIdValidationError(trimmed)?.toUiText()
            } else null
            updateState { it.copy(nationalId = trimmed, nationalIdError = error) }
        }
    }

    override fun onJobChanged(value: String) = updateState { it.copy(job = value) }
    override fun onIsFromAnotherChurchChanged(value: Boolean) = updateState { it.copy(isFromAnotherChurch = value, confessionPriestError = null) }
    override fun onConfessionPriestIdChanged(value: String?) = updateState { it.copy(confessionPriestId = value, confessionPriestError = null) }
    override fun onConfessionPriestNameChanged(value: String) = updateState { it.copy(confessionPriestName = value, externalPriestNameError = null) }
    override fun onConfessionPriestChurchChanged(value: String) = updateState { it.copy(confessionPriestChurch = value, externalPriestChurchError = null) }

    override fun onConfessionPriestPhoneChanged(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(confessionPriestPhone = value, externalPriestPhoneError = null) }
        }
    }

    override fun onPhoneChanged(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(phone = value, phoneError = null) }
        }
    }

    override fun onHomePhoneChanged(value: String) {
        if (value.isEmpty() || value.length <= 8) {
            updateState { it.copy(homePhone = value, homePhoneError = null) }
        }
    }

    override fun onEmailChanged(email: String) {
        if (email.isEmpty() || isValidEmailInput(email)) {
            updateState { it.copy(email = email, emailError = null) }
        }
    }

    override fun onPasswordChanged(password: String) {
        val error = getPasswordValidationError(password)
        updateState { it.copy(password = password, passwordError = error?.toUiText()) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    override fun onBuildingNoChanged(value: String) {
        if (isValidBuildingNoInput(value)) {
            updateState { it.copy(buildingNo = value, buildingNoError = null) }
        }
    }

    override fun onStreetChanged(value: String) = updateState { it.copy(street = value, streetError = null) }
    override fun onStreetBranchChanged(value: String) = updateState { it.copy(streetBranch = value) }

    override fun onAreaChanged(value: String) {
        updateState {
            it.copy(
                area = value,
                areaError = null,
                isAreaSheetVisible = false,
                areas = emptyList()
            )
        }
        searchAreas(value)
    }

    private fun searchAreas(query: String) {
        tryToCall(
            block = {
                lookupRepository.getAreas(query = query, pageQuery = PageQuery(page = 0, size = 20)).toPagedData()
            },
            onStart = { updateState { it.copy(isAreaLoading = true) } },
            onSuccess = { items ->
                updateState { it.copy(areas = items.data, isAreaSheetVisible = true) }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_areas),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { it.copy(isAreaLoading = false) } }
        )
    }

    override fun onSelectArea(area: String) {
        updateState { it.copy(area = area, areaError = null, isAreaSheetVisible = false) }
    }

    override fun onToggleAreaSheet(visible: Boolean) {
        updateState { it.copy(isAreaSheetVisible = visible) }
    }

    override fun onSelectConfessionPriest(priest: Priest?) {
        updateState {
            it.copy(
                selectedConfessionPriest = priest,
                confessionPriestId = priest?.id,
                confessionPriestName = priest?.name ?: "",
                isFromAnotherChurch = false,
                confessionPriestError = null,
                isPriestSheetVisible = false
            )
        }
    }

    override fun onSelectFromAnotherChurch() {
        updateState {
            it.copy(
                selectedConfessionPriest = null,
                confessionPriestId = null,
                isFromAnotherChurch = true,
                confessionPriestError = null,
                isPriestSheetVisible = false
            )
        }
    }

    override fun onTogglePriestSheet(visible: Boolean) {
        updateState { it.copy(isPriestSheetVisible = visible) }
    }

    override fun onFloorChanged(value: String) {
        if (isValidFloorInput(value)) {
            updateState { it.copy(floor = value, floorError = null) }
        }
    }

    override fun onApartmentChanged(value: String) {
        if (isValidApartmentInput(value)) {
            updateState { it.copy(apartment = value) }
        }
    }

    override fun onSpecialMarkChanged(value: String) = updateState { it.copy(specialMark = value, specialMarkError = null) }

    override fun onRoleSelected(role: UserRole) {
        if (state.value.selectedRole != role) {
            rawEducationalStages = emptyList()
            updateState {
                it.copy(
                    selectedRole = role,
                    educationalStages = emptyList(),
                    isStageEndReached = false
                )
            }
            stagesPaginator.reset()
            onLoadNextEducationalStages()
        }
    }

    override fun onToggleRoleSheet(visible: Boolean) {
        updateState { it.copy(isRoleSheetVisible = visible) }
    }

    override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {
        updateState { it.copy(shamamsaStatus = status) }
    }

    override fun onToggleOrdained(ordained: Boolean) {
        updateState { it.copy(isOrdained = ordained) }
    }

    override fun onSelectRank(rank: LookupResponse) {
        updateState { it.copy(selectedRank = rank, rankError = null) }
    }

    override fun onToggleRankSheet(visible: Boolean) {
        updateState { it.copy(isRankSheetVisible = visible) }
    }

    override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {
        updateState { it.copy(isOrdainedInThisChurch = inThisChurch) }
    }

    override fun onOrdinationYearChange(value: String) {
        updateState { it.copy(ordinationYear = value, ordinationYearError = null) }
    }

    override fun onBishopNameChange(value: String) {
        updateState { it.copy(bishopName = value) }
    }

    override fun onOrdinationPlaceChange(value: String) {
        updateState { it.copy(ordinationPlace = value) }
    }

    override fun onSelectEducationalStage(stage: LookupResponse) {
        val currentYear = state.value.studentEducationalYear
        val validYear = if (currentYear != null && stage.subItems.any { it.id == currentYear.id }) currentYear else null
        updateState { it.copy(studentEducationalStage = stage, studentEducationalYear = validYear, stageError = null) }
    }

    override fun onToggleStageSheet(visible: Boolean) {
        updateState { it.copy(isStageSheetVisible = visible) }
    }

    override fun onSelectEducationalYear(year: LookupResponse) {
        updateState { it.copy(studentEducationalYear = year, yearError = null) }
    }

    override fun onToggleYearSheet(visible: Boolean) {
        updateState { it.copy(isYearSheetVisible = visible) }
    }

    override fun onToggleFatherDeceased(deceased: Boolean) {
        updateState { it.copy(isFatherDeceased = deceased, fatherPhoneError = null, fatherWhatsappError = null) }
    }

    override fun onFatherPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(fatherPhone = value, fatherPhoneError = null) }
        }
    }

    override fun onFatherWhatsappChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(fatherWhatsapp = value, fatherWhatsappError = null) }
        }
    }

    override fun onToggleMotherDeceased(deceased: Boolean) {
        updateState { it.copy(isMotherDeceased = deceased, motherPhoneError = null, motherWhatsappError = null) }
    }

    override fun onMotherPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(motherPhone = value, motherPhoneError = null) }
        }
    }

    override fun onMotherWhatsappChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { it.copy(motherWhatsapp = value, motherWhatsappError = null) }
        }
    }

    override fun onToggleServantStageSelection(stage: LookupResponse) {
        updateState { current ->
            val currentStage = current.servantEducationalStage
            val updated = if (currentStage?.id == stage.id) null else stage
            updateDerivedProperties(current.copy(servantEducationalStage = updated, stageError = null))
        }
    }

    override fun onToggleServantStageSheet(visible: Boolean) {
        updateState { it.copy(isServantStageSheetVisible = visible) }
    }

    override fun onToggleServantYearSelection(year: LookupResponse) {
        updateState { current ->
            val currentYear = current.servantEducationalYear
            val updated = if (currentYear?.id == year.id) null else year
            current.copy(servantEducationalYear = updated, yearError = null)
        }
    }

    override fun onToggleServantYearSheet(visible: Boolean) {
        updateState { it.copy(isServantYearSheetVisible = visible) }
    }

    override fun onToggleCanApproveNewRequests(canApprove: Boolean) {
        updateState { it.copy(canApproveNewRequests = canApprove) }
    }

    override fun onToggleResponsibleStageSelection(stage: LookupResponse) {
        updateState { current ->
            val list = current.responsibleStages
            val updated = if (list.any { it.id == stage.id }) list.filterNot { it.id == stage.id } else list + stage
            current.copy(responsibleStages = updated)
        }
    }

    override fun onToggleResponsibleStageSheet(visible: Boolean) {
        updateState { it.copy(isResponsibleStageSheetVisible = visible) }
    }

    override fun onToggleResponsibleYearSelection(year: LookupResponse) {
        updateState { current ->
            val list = current.responsibleYears
            val updated = if (list.any { it.id == year.id }) list.filterNot { it.id == year.id } else list + year
            current.copy(responsibleYears = updated)
        }
    }

    override fun onToggleResponsibleYearSheet(visible: Boolean) {
        updateState { it.copy(isResponsibleYearSheetVisible = visible) }
    }

    override fun onPartnerQueryChange(query: String) {
        updateState { it.copy(partnerQuery = query) }
    }

    override fun onSearchPartner() {
        if (state.value.partnerQuery.isNotBlank()) {
            tryToCall(
                block = { registerRepository.searchParent(state.value.partnerQuery) },
                onSuccess = { res ->
                    if (res != null) updateState { it.copy(selectedPartner = res) }
                },
                onError = { throwable ->
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_search_partner),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                }
            )
        }
    }

    override fun onRemovePartner() {
        updateState { it.copy(selectedPartner = null, partnerQuery = "") }
    }

    override fun onChildQueryChange(query: String) {
        updateState { it.copy(childQuery = query) }
    }

    override fun onSearchChild() {
        if (state.value.childQuery.isNotBlank()) {
            tryToCall(
                block = { registerRepository.searchMakhdoom(state.value.childQuery) },
                onSuccess = { res ->
                    if (res != null) updateState {
                        it.copy(
                            selectedChildren = it.selectedChildren + res,
                            childQuery = ""
                        )
                    }
                },
                onError = { throwable ->
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_search_child),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                }
            )
        }
    }

    override fun onRemoveChild(child: UserSummary) {
        updateState { it.copy(selectedChildren = it.selectedChildren - child) }
    }

    override fun onToggleEducationalStageSelection(stage: LookupResponse) {
        updateState { current ->
            val currentStages = current.kahenEducationalStages
            val updatedStages = if (currentStages.any { it.id == stage.id }) {
                currentStages.filterNot { it.id == stage.id }
            } else {
                currentStages + stage
            }
            current.copy(kahenEducationalStages = updatedStages, stagesError = null)
        }
    }

    override fun onToggleStagesSheet(visible: Boolean) {
        updateState { it.copy(isStagesSheetVisible = visible) }
    }

    override fun onLoadNextEducationalStages() {
        launch {
            stagesPaginator.loadNextItems()
        }
    }

    override fun onLoadNextRanks() {
        launch {
            ranksPaginator.loadNextItems()
        }
    }

    override fun onNotesChanged(value: String) {
        updateState { it.copy(notes = value) }
    }
}
