package com.teEcclesia.identity.presentation.screen.register

import androidx.lifecycle.viewModelScope
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.domain.model.CompleteProfileRequest
import com.teEcclesia.identity.domain.model.KahenProfileRequest
import com.teEcclesia.identity.domain.model.KhademProfileRequest
import com.teEcclesia.identity.domain.model.MakhdoomProfileRequest
import com.teEcclesia.identity.domain.model.OrdinationProfileRequest
import com.teEcclesia.identity.domain.model.ParentProfileRequest
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.identity.presentation.util.toUiText
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.validation.getPasswordValidationError
import com.teEcclesia.shared.domain.utils.validation.getNationalIdValidationError
import com.teEcclesia.shared.domain.utils.validation.isMaleFromEgyptianNationalId
import com.teEcclesia.shared.domain.utils.validation.isValidEmailInput
import com.teEcclesia.shared.domain.utils.validation.isValidFinalEmail
import com.teEcclesia.shared.domain.utils.validation.isValidNationalIdInput
import com.teEcclesia.shared.domain.utils.validation.isValidPhoneInput
import com.teEcclesia.shared.domain.utils.validation.validateArabicName
import com.teEcclesia.shared.domain.utils.validation.validatePhone
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitCameraType
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openCameraPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_complete_profile
import teecclesia.designsystem.generated.resources.failed_to_load_areas
import teecclesia.designsystem.generated.resources.failed_to_load_priests
import teecclesia.designsystem.generated.resources.failed_to_load_ranks
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import teecclesia.designsystem.generated.resources.failed_to_register
import teecclesia.designsystem.generated.resources.failed_to_search_child
import teecclesia.designsystem.generated.resources.failed_to_search_partner
import teecclesia.designsystem.generated.resources.failed_to_verify_whatsapp
import teecclesia.designsystem.generated.resources.field_required
import teecclesia.designsystem.generated.resources.invalid_arabic_name
import teecclesia.designsystem.generated.resources.invalid_email_format
import teecclesia.designsystem.generated.resources.invalid_home_phone_format
import teecclesia.designsystem.generated.resources.invalid_national_id_format
import teecclesia.designsystem.generated.resources.invalid_phone_format
import teecclesia.designsystem.generated.resources.invalid_year_format

class RegisterViewModel(
    private val isEditMode: Boolean,
    private val registerRepository: RegisterRepository,
    private val lookupRepository: LookupRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val authorizationService: AuthorizationService,
    private val profileRepository: ProfileRepository
) : BaseViewModel<RegisterScreenState>(RegisterScreenState()), RegisterInteractionListener {

    private var whatsappToken: String? = null

    private val priestsPaginator = createPaginator(
        loadPage = { page ->
            registerRepository.getConfessionPriests(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState {
                copy(
                    confessionPriests = confessionPriests + items.data,
                    isPriestEndReached = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { copy(isPriestLoading = loading) }
        },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_priests),
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
            updateState {
                copy(
                    ranks = ranks + items.data,
                    isRankEndReached = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { copy(isRankLoading = loading) }
        },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_ranks),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    )

    private val stagesPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getEducationalStages(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState {
                copy(
                    educationalStages = educationalStages + items.data,
                    isStageEndReached = items.isLastPage,
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { copy(isStageLoading = loading) }
        },
        onError = { throwable ->
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_educational_stages),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    )

    init {
        checkAndLoadPendingRegistration()
        onLoadNextPriests()
        onLoadNextRanks()
        onLoadNextEducationalStages()
    }

    private fun checkAndLoadPendingRegistration() {
        tryToCall(
            block = {
                if (isEditMode || authorizationService.isRegistrationPending()) {
                    val profile = profileRepository.getRegistrationProfile()
                    if (profile.status == UserStatus.PENDING_APPROVAL && !isEditMode) {
                        resetTo(PendingApprovalRoute)
                        return@tryToCall
                    }
                    val hasSelectedSpecificRole = profile.role in setOf(
                        UserRole.KHADEM,
                        UserRole.MAKHDOOM,
                        UserRole.PARENT,
                        UserRole.KAHEN
                    )
                    val targetStep = if (isEditMode) 4 else when {
                        profile.isPhoneVerified -> 5
                        profile.role == UserRole.KHADEM && profile.khademProfile != null -> 5
                        profile.role == UserRole.PARENT && profile.parentProfile != null -> 5
                        profile.role == UserRole.KAHEN && profile.kahenProfile != null -> 5
                        profile.role == UserRole.MAKHDOOM && profile.makhdoomProfile != null -> 5
                        profile.phone.isNotBlank() && profile.buildingNo.isNotBlank() -> if (hasSelectedSpecificRole) 4 else 3
                        profile.firstName.isNotBlank() && profile.lastName.isNotBlank() -> 2
                        else -> 1
                    }
                    val cleanedPhone = profile.phone.removePrefix("+2")
                    val cleanedHomePhone = profile.homePhone.removePrefix("02")
                    val makhdoom = profile.makhdoomProfile
                    val ordination = profile.ordinationProfile
                    val khadem = profile.khademProfile
                    updateState {
                        copy(
                            firstName = profile.firstName,
                            secondName = profile.secondName,
                            thirdName = profile.thirdName,
                            lastName = profile.lastName,
                            displayName = profile.displayName,
                            nationalId = profile.nationalId,
                            job = profile.job,
                            isMale = isMaleFromEgyptianNationalId(profile.nationalId),
                            selectedConfessionPriest = profile.confessionPriest,
                            isFromAnotherChurch = profile.confessionPriest == null && profile.externalConfessionPriestName.isNotBlank(),
                            externalPriestName = profile.externalConfessionPriestName,
                            externalPriestChurch = profile.externalConfessionChurch,
                            externalPriestPhone = profile.externalConfessionPhone,
                            phone = cleanedPhone,
                            homePhone = cleanedHomePhone,
                            email = profile.email,
                            imageUrl = profile.imageUrl,
                            buildingNo = profile.buildingNo,
                            street = profile.street,
                            streetBranch = profile.streetBranch,
                            selectedArea = profile.area,
                            floor = profile.floor,
                            apartment = profile.apartment,
                            specialMark = profile.specialMark,
                            selectedRole = if (hasSelectedSpecificRole) profile.role else null,

                            // Makhdoom / Student fields
                            isFatherDeceased = makhdoom?.isFatherDeceased ?: false,
                            fatherPhone = makhdoom?.fatherPhone?.removePrefix("+2") ?: "",
                            fatherWhatsapp = makhdoom?.fatherWhatsapp?.removePrefix("+2") ?: "",
                            isMotherDeceased = makhdoom?.isMotherDeceased ?: false,
                            motherPhone = makhdoom?.motherPhone?.removePrefix("+2") ?: "",
                            motherWhatsapp = makhdoom?.motherWhatsapp?.removePrefix("+2") ?: "",
                            shamamsaStatus = makhdoom?.shamamsaStudyStatus
                                ?: ShamamsaStudyStatus.YES,
                            studentEducationalStage = makhdoom?.educationalStage,
                            studentEducationalYear = makhdoom?.educationalYear,

                            // Servant fields
                            servantEducationalStage = khadem?.educationalStage,
                            servantEducationalYear = khadem?.educationalYear,

                            // Ordination fields
                            isOrdained = ordination != null,
                            selectedRank = ordination?.rank,
                            isOrdainedInThisChurch = ordination?.isOrdinationInAnotherChurch?.not()
                                ?: true,
                            ordinationYear = ordination?.ordinationYear?.toString() ?: "",
                            bishopName = ordination?.bishopName ?: "",
                            ordinationPlace = ordination?.ordinationPlace ?: "",

                            kahenEducationalStages = profile.kahenProfile?.educationalStages
                                ?: emptyList(),
                            currentStep = targetStep
                        )
                    }
                    if (state.value.currentStep == 5) {
                        initiateWhatsAppStep5()
                    }
                }
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = {},
            onError = { /* Ignore error on pending profile fetch */ },
            onEnd = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onLoadNextPriests() {
        viewModelScope.launch {
            if (!state.value.isPriestEndReached && !state.value.isPriestLoading) {
                priestsPaginator.loadNextItems()
            }
        }
    }

    override fun onLoadNextRanks() {
        viewModelScope.launch {
            if (!state.value.isRankEndReached && !state.value.isRankLoading) {
                ranksPaginator.loadNextItems()
            }
        }
    }

    override fun onLoadNextEducationalStages() {
        viewModelScope.launch {
            if (!state.value.isStageEndReached && !state.value.isStageLoading) {
                stagesPaginator.loadNextItems()
            }
        }
    }

    override fun onClickNextStep() {
        when (state.value.currentStep) {
            1 -> validateAndAdvanceStep1()
            2 -> validateAndSubmitRegisterStep2()
            3 -> advanceStep3()
            4 -> submitCompleteProfileStep4()
        }
    }

    override fun onClickPreviousStep() {
        if (state.value.currentStep > 1) {
            val prevStep =
                if (state.value.currentStep == 5 && state.value.selectedRole == UserRole.KAHEN) {
                    3
                } else {
                    state.value.currentStep - 1
                }
            updateState { copy(currentStep = prevStep) }
        } else {
            popBackStack()
        }
    }

    override fun onClickLogin() {
        resetTo(LoginRoute)
    }

    // Step 1 logic
    private fun validateAndAdvanceStep1() {
        val s = state.value
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
        val isMale = isMaleFromEgyptianNationalId(s.nationalId)

        val confessionPriestErr =
            if (!s.isFromAnotherChurch && s.selectedConfessionPriest == null) UiText.StringRes(Res.string.field_required) else null
        val externalNameErr =
            if (s.isFromAnotherChurch && s.externalPriestName.isBlank()) UiText.StringRes(Res.string.field_required) else null
        val externalChurchErr =
            if (s.isFromAnotherChurch && s.externalPriestChurch.isBlank()) UiText.StringRes(Res.string.field_required) else null
        val externalPhoneErr =
            if (s.isFromAnotherChurch && !validatePhone(s.externalPriestPhone)) UiText.StringRes(Res.string.invalid_phone_format) else null

        val hasError = listOfNotNull(
            firstNameError,
            secondNameError,
            thirdNameError,
            lastNameError,
            displayNameError,
            nationalIdError,
            confessionPriestErr,
            externalNameErr,
            externalChurchErr,
            externalPhoneErr
        ).isNotEmpty()

        updateState {
            copy(
                firstNameError = firstNameError,
                secondNameError = secondNameError,
                thirdNameError = thirdNameError,
                lastNameError = lastNameError,
                displayNameError = displayNameError,
                nationalIdError = nationalIdError,
                isMale = isMale,
                confessionPriestError = confessionPriestErr,
                externalPriestNameError = externalNameErr,
                externalPriestChurchError = externalChurchErr,
                externalPriestPhoneError = externalPhoneErr
            )
        }

        if (!hasError) {
            updateState { copy(currentStep = 2) }
        }
    }

    override fun onFirstNameChange(value: String) {
        val value = value.trim()
        if (value.isEmpty() || validateArabicName(name = value)) updateState {
            copy(
                firstName = value,
                firstNameError = null
            )
        }
    }

    override fun onSecondNameChange(value: String) {
        val value = value.trim()
        if (value.isEmpty() || validateArabicName(name = value)) updateState {
            copy(
                secondName = value,
                secondNameError = null
            )
        }
    }

    override fun onThirdNameChange(value: String) {
        val value = value.trim()
        if (value.isEmpty() || validateArabicName(name = value)) updateState {
            copy(
                thirdName = value,
                thirdNameError = null
            )
        }
    }

    override fun onLastNameChange(value: String) {
        val value = value.trim()
        if (value.isEmpty() || validateArabicName(name = value)) updateState {
            copy(
                lastName = value,
                lastNameError = null
            )
        }
    }

    override fun onDisplayNameChange(value: String) {
        updateState { copy(displayName = value, displayNameError = null) }
    }

    override fun onNationalIdChange(value: String) {
        val value = value.trim()
        if (value.isEmpty() || isValidNationalIdInput(value)) {
            val error = if (value.length == 14) {
                getNationalIdValidationError(value)?.toUiText()
            } else {
                null
            }
            val isMale = isMaleFromEgyptianNationalId(value)
            updateState {
                copy(
                    nationalId = value,
                    nationalIdError = error,
                    isMale = isMale
                )
            }
        }
    }

    override fun onJobChange(value: String) {
        updateState { copy(job = value) }
    }

    override fun onSelectConfessionPriest(priest: Priest?) {
        updateState {
            copy(
                selectedConfessionPriest = priest,
                isFromAnotherChurch = false,
                confessionPriestError = null
            )
        }
    }

    override fun onSelectFromAnotherChurch() {
        updateState {
            copy(
                isFromAnotherChurch = true,
                selectedConfessionPriest = null,
                confessionPriestError = null
            )
        }
    }

    override fun onExternalPriestNameChange(value: String) {
        updateState { copy(externalPriestName = value, externalPriestNameError = null) }
    }

    override fun onExternalPriestChurchChange(value: String) {
        updateState { copy(externalPriestChurch = value, externalPriestChurchError = null) }
    }

    override fun onExternalPriestPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(externalPriestPhone = value, externalPriestPhoneError = null) }
        }
    }

    override fun onTogglePriestSheet(visible: Boolean) {
        updateState { copy(isPriestSheetVisible = visible) }
    }

    // Step 2 logic
    private fun validateAndSubmitRegisterStep2() {
        val s = state.value
        val phoneErr =
            if (validatePhone(s.phone)) null else UiText.StringRes(Res.string.invalid_phone_format)
        val homePhoneErr =
            if (s.homePhone.isBlank() || s.homePhone.length == 8) null else UiText.StringRes(Res.string.invalid_home_phone_format)
        val emailErr =
            if (s.email.isNotBlank() && isValidFinalEmail(s.email) || s.email.isBlank()) null else UiText.StringRes(
                Res.string.invalid_email_format
            )
        val passErr =
            getPasswordValidationError(s.password)?.toUiText()
        val buildingErr =
            if (s.buildingNo.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val streetErr =
            if (s.street.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val areaErr =
            if (s.selectedArea != null) null else UiText.StringRes(Res.string.field_required)
        val floorErr =
            if (s.floor.isNotBlank()) null else UiText.StringRes(Res.string.field_required)
        val markErr =
            if (s.specialMark.isNotBlank()) null else UiText.StringRes(Res.string.field_required)

        val hasError = listOfNotNull(
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
            copy(
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

        if (!hasError) {
            val registerRequest = RegisterRequest(
                firstName = s.firstName,
                secondName = s.secondName,
                thirdName = s.thirdName,
                lastName = s.lastName,
                displayName = s.displayName,
                nationalId = s.nationalId,
                job = s.job,
                confessionPriestId = s.selectedConfessionPriest?.id,
                externalConfessionPriestName = if (s.isFromAnotherChurch) s.externalPriestName else null,
                externalConfessionChurch = if (s.isFromAnotherChurch) s.externalPriestChurch else null,
                externalConfessionPhone = if (s.isFromAnotherChurch) s.externalPriestPhone else null,
                phone = s.phone,
                homePhone = s.homePhone,
                email = s.email.ifBlank { null },
                password = s.password,
                imageUrl = null,
                buildingNo = s.buildingNo,
                street = s.street,
                streetBranch = s.streetBranch.ifBlank { null },
                area = s.selectedArea ?: "",
                floor = s.floor,
                apartment = s.apartment.ifBlank { null },
                specialMark = s.specialMark,
                role = null,
                ordinationProfile = null,
                makhdoomProfile = null,
                parentProfile = null
            )

            tryToCall(
                block = {
                    val tokenResponse = registerRepository.register(
                        request = registerRequest,
                        imageBytes = s.imageBytes,
                        certificateImageBytes = null
                    )
                    authenticationRepository.saveRegistrationToken(
                        tokenResponse.token,
                        tokenResponse.refreshToken ?: ""
                    )
                },
                onStart = { updateState { copy(isLoading = true, actionButtonState = AppButtonState.Loading) } },
                onSuccess = {
                    updateState { copy(currentStep = 3) }
                },
                onError = { throwable ->
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_register),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                },
                onEnd = { updateState { copy(isLoading = false, actionButtonState = AppButtonState.Enabled) } }
            )
        }
    }

    override fun onPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(phone = value, phoneError = null) }
        }
    }

    override fun onHomePhoneChange(value: String) {
        if (value.isEmpty() || value.length <= 8) {
            updateState { copy(homePhone = value, homePhoneError = null) }
        }
    }

    override fun onEmailChange(value: String) {
        if (value.isEmpty() || isValidEmailInput(value)) {
            updateState { copy(email = value, emailError = null) }
        }
    }

    override fun onPasswordChange(value: String) {
        val error = getPasswordValidationError(value)
        updateState { copy(password = value, passwordError = error?.toUiText()) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onBuildingNoChange(value: String) {
        if (value.isEmpty() || value.length <= 5 || value.all { it.isDigit() }) {
            updateState { copy(buildingNo = value, buildingNoError = null) }
        }
    }

    override fun onStreetChange(value: String) {
        updateState { copy(street = value, streetError = null) }
    }

    override fun onStreetBranchChange(value: String) {
        updateState { copy(streetBranch = value) }
    }

    override fun onAreaChange(value: String) {
        updateState {
            copy(
                selectedArea = value,
                areaError = null,
                isAreaSheetVisible = false,
                areas = emptyList(),
            )
        }
        searchAreas(value)
    }

    private fun searchAreas(query: String) {
        tryToCall(
            block = {
                lookupRepository.getAreas(query = query, pageQuery = PageQuery(page = 0, size = 20))
                    .toPagedData()
            },
            onStart = { updateState { copy(isAreaLoading = true) } },
            onSuccess = { items ->
                updateState {
                    copy(areas = items.data, isAreaSheetVisible = true)
                }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_areas),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(isAreaLoading = false) } }
        )
    }

    override fun onSelectArea(area: String) {
        updateState { copy(selectedArea = area, areaError = null, isAreaSheetVisible = false) }
    }

    override fun onToggleAreaSheet(visible: Boolean) {
        updateState { copy(isAreaSheetVisible = visible) }
    }

    override fun onFloorChange(value: String) {
        if (value.isEmpty() || value.length <= 2 || value.all { it.isDigit() }) {
            updateState { copy(floor = value, floorError = null) }
        }
    }

    override fun onApartmentChange(value: String) {
        if (value.isEmpty() || value.length <= 3 || value.all { it.isDigit() }) {
            updateState { copy(apartment = value) }
        }
    }

    override fun onSpecialMarkChange(value: String) {
        updateState { copy(specialMark = value, specialMarkError = null) }
    }

    // Step 3 logic
    private fun advanceStep3() {
        if (state.value.selectedRole != null) {
            updateState { copy(currentStep = 4) }
        }
    }

    override fun onRoleSelected(role: UserRole) {
        updateState { copy(selectedRole = role) }
    }

    // Step 4 logic
    private fun submitCompleteProfileStep4() {
        val s = state.value
        val role = s.selectedRole ?: return

        when (role) {
            UserRole.KHADEM -> {
                val stageErr =
                    if (s.servantEducationalStage == null) UiText.StringRes(Res.string.field_required) else null
                val yearErr =
                    if (!s.servantEducationalStage?.subItems.isNullOrEmpty() && s.servantEducationalYear == null) {
                        UiText.StringRes(Res.string.field_required)
                    } else null

                val hasError = listOfNotNull(stageErr, yearErr).isNotEmpty()
                updateState {
                    copy(
                        stageError = stageErr,
                        yearError = yearErr
                    )
                }
                if (hasError) return
            }

            UserRole.MAKHDOOM -> {
                val rankErr =
                    if (s.isOrdained && s.selectedRank == null) UiText.StringRes(Res.string.field_required) else null
                val stageErr =
                    if (s.studentEducationalStage == null) UiText.StringRes(Res.string.field_required) else null
                val yearErr =
                    if (!s.studentEducationalStage?.subItems.isNullOrEmpty() && s.studentEducationalYear == null) {
                        UiText.StringRes(Res.string.field_required)
                    } else null

                val ordinationYearErr = if (s.isOrdained) {
                    if (s.ordinationYear.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (s.ordinationYear.length != 4 || !s.ordinationYear.all { it.isDigit() }) {
                        UiText.StringRes(Res.string.invalid_year_format)
                    } else null
                } else null

                val fatherPhoneErr = if (!s.isFatherDeceased) {
                    if (s.fatherPhone.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (!validatePhone(s.fatherPhone)) {
                        UiText.StringRes(Res.string.invalid_phone_format)
                    } else null
                } else null

                val fatherWhatsappErr = if (!s.isFatherDeceased) {
                    if (s.fatherWhatsapp.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (!validatePhone(s.fatherWhatsapp)) {
                        UiText.StringRes(Res.string.invalid_phone_format)
                    } else null
                } else null

                val motherPhoneErr = if (!s.isMotherDeceased) {
                    if (s.motherPhone.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (!validatePhone(s.motherPhone)) {
                        UiText.StringRes(Res.string.invalid_phone_format)
                    } else null
                } else null

                val motherWhatsappErr = if (!s.isMotherDeceased) {
                    if (s.motherWhatsapp.isBlank()) {
                        UiText.StringRes(Res.string.field_required)
                    } else if (!validatePhone(s.motherWhatsapp)) {
                        UiText.StringRes(Res.string.invalid_phone_format)
                    } else null
                } else null

                val hasError = listOfNotNull(
                    rankErr, stageErr, yearErr, ordinationYearErr,
                    fatherPhoneErr, fatherWhatsappErr, motherPhoneErr, motherWhatsappErr
                ).isNotEmpty()
                updateState {
                    copy(
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
                if (hasError) return
            }

            UserRole.PARENT -> {
                // Parent profile validation
            }

            UserRole.KAHEN -> {
                val stageErr = if (s.kahenEducationalStages.isEmpty()) {
                    UiText.StringRes(Res.string.field_required)
                } else null
                if (stageErr != null) {
                    updateState { copy(stageError = stageErr) }
                    return
                }
            }

            else -> {}
        }

        val request = CompleteProfileRequest(
            role = role,
            khademProfile = if (role == UserRole.KHADEM) KhademProfileRequest(
                educationalStageId = s.servantEducationalStage?.id ?: 1L,
                educationalYearId = s.servantEducationalYear?.id
            ) else null,
            kahenProfile = if (role == UserRole.KAHEN) KahenProfileRequest(
                educationalStageIds = s.kahenEducationalStages.map { it.id }
            ) else null,
            ordinationProfile = if (role != UserRole.KAHEN && role != UserRole.PARENT && s.isOrdained) OrdinationProfileRequest(
                rankId = s.selectedRank?.id ?: 1L,
                isOrdinationInAnotherChurch = !s.isOrdainedInThisChurch,
                ordinationYear = s.ordinationYear.toIntOrNull(),
                bishopName = s.bishopName.ifBlank { null },
                ordinationPlace = s.ordinationPlace.ifBlank { null }
            ) else null,
            makhdoomProfile = if (role == UserRole.MAKHDOOM) MakhdoomProfileRequest(
                shamamsaStudyStatus = s.shamamsaStatus,
                educationalStageId = s.studentEducationalStage?.id ?: 1L,
                educationalYearId = s.studentEducationalYear?.id,
                isFatherDeceased = s.isFatherDeceased,
                fatherPhone = s.fatherPhone.ifBlank { null },
                fatherWhatsapp = s.fatherWhatsapp.ifBlank { null },
                isMotherDeceased = s.isMotherDeceased,
                motherPhone = s.motherPhone.ifBlank { null },
                motherWhatsapp = s.motherWhatsapp.ifBlank { null }
            ) else null,
            parentProfile = if (role == UserRole.PARENT) ParentProfileRequest(
                partnerCode = s.selectedPartner?.code,
                childrenCodes = s.selectedChildren.mapNotNull { it.code }
            ) else null
        )

        tryToCall(
            block = {
                registerRepository.completeProfile(
                    request = request,
                    certificateImageBytes = s.ordinationCertificateBytes
                        ?: s.identityCertificateBytes
                )
            },
            onStart = { updateState { copy(isLoading = true, actionButtonState = AppButtonState.Loading) } },
            onSuccess = {
                initiateWhatsAppStep5()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_complete_profile),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(isLoading = false, actionButtonState = AppButtonState.Enabled) } }
        )
    }

    override fun onToggleOrdained(ordained: Boolean) {
        updateState {
            copy(
                isOrdained = ordained,
                ordinationYearError = if (!ordained) null else ordinationYearError,
                rankError = if (!ordained) null else rankError
            )
        }
    }

    override fun onSelectRank(rank: LookupResponse) {
        updateState { copy(selectedRank = rank, rankError = null) }
    }

    override fun onToggleRankSheet(visible: Boolean) {
        updateState { copy(isRankSheetVisible = visible) }
    }

    override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {
        updateState { copy(isOrdainedInThisChurch = inThisChurch) }
    }

    override fun onOrdinationYearChange(value: String) {
        if (value.isEmpty() || (value.all { it.isDigit() } && value.length <= 4)) {
            updateState { copy(ordinationYear = value, ordinationYearError = null) }
        }
    }

    override fun onBishopNameChange(value: String) {
        updateState { copy(bishopName = value) }
    }

    override fun onOrdinationPlaceChange(value: String) {
        updateState { copy(ordinationPlace = value) }
    }

    override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {
        updateState { copy(shamamsaStatus = status) }
    }

    override fun onSelectEducationalStage(stage: LookupResponse) {
        updateState {
            val role = selectedRole
            copy(
                studentEducationalStage = if (role == UserRole.MAKHDOOM) stage else studentEducationalStage,
                studentEducationalYear = if (role == UserRole.MAKHDOOM) null else studentEducationalYear,
                servantEducationalStage = if (role == UserRole.KHADEM) stage else servantEducationalStage,
                servantEducationalYear = if (role == UserRole.KHADEM) null else servantEducationalYear,
                stageError = null,
                yearError = null
            )
        }
    }

    override fun onToggleEducationalStageSelection(stage: LookupResponse) {
        updateState {
            val currentList = kahenEducationalStages
            val newList = if (currentList.any { it.id == stage.id }) {
                currentList.filterNot { it.id == stage.id }
            } else {
                currentList + stage
            }
            copy(
                kahenEducationalStages = if (selectedRole == UserRole.KAHEN) newList else kahenEducationalStages,
                stageError = null,
                stagesError = null
            )
        }
    }

    override fun onToggleStagesSheet(visible: Boolean) {
        if (visible && state.value.educationalStages.isEmpty()) {
            onLoadNextEducationalStages()
        }
        updateState { copy(isStagesSheetVisible = visible) }
    }

    override fun onToggleStageSheet(visible: Boolean) {
        if (visible && state.value.educationalStages.isEmpty()) {
            onLoadNextEducationalStages()
        }
        updateState { copy(isStageSheetVisible = visible) }
    }

    override fun onSelectEducationalYear(year: LookupResponse) {
        updateState {
            val role = selectedRole
            copy(
                studentEducationalYear = if (role == UserRole.MAKHDOOM) year else studentEducationalYear,
                servantEducationalYear = if (role == UserRole.KHADEM) year else servantEducationalYear,
                yearError = null
            )
        }
    }

    override fun onToggleYearSheet(visible: Boolean) {
        updateState { copy(isYearSheetVisible = visible) }
    }

    override fun onToggleFatherDeceased(deceased: Boolean) {
        updateState {
            copy(
                isFatherDeceased = deceased,
                fatherPhoneError = if (deceased) null else fatherPhoneError,
                fatherWhatsappError = if (deceased) null else fatherWhatsappError
            )
        }
    }

    override fun onFatherPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(fatherPhone = value, fatherPhoneError = null) }
        }
    }

    override fun onFatherWhatsappChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(fatherWhatsapp = value, fatherWhatsappError = null) }
        }
    }

    override fun onToggleMotherDeceased(deceased: Boolean) {
        updateState {
            copy(
                isMotherDeceased = deceased,
                motherPhoneError = if (deceased) null else motherPhoneError,
                motherWhatsappError = if (deceased) null else motherWhatsappError
            )
        }
    }

    override fun onMotherPhoneChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(motherPhone = value, motherPhoneError = null) }
        }
    }

    override fun onMotherWhatsappChange(value: String) {
        if (value.isEmpty() || isValidPhoneInput(value)) {
            updateState { copy(motherWhatsapp = value, motherWhatsappError = null) }
        }
    }

    override fun onPartnerQueryChange(query: String) {
        updateState { copy(partnerQuery = query) }
    }

    override fun onSearchPartner() {
        if (state.value.partnerQuery.isNotBlank()) {
            tryToCall(
                block = { registerRepository.searchParent(state.value.partnerQuery) },
                onSuccess = { res ->
                    if (res != null) updateState { copy(selectedPartner = res) }
                },
                onError = {
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_search_partner),
                        isSuccess = false
                    )
                }
            )
        }
    }

    override fun onRemovePartner() {
        updateState { copy(selectedPartner = null, partnerQuery = "") }
    }

    override fun onChildQueryChange(query: String) {
        updateState { copy(childQuery = query) }
    }

    override fun onSearchChild() {
        if (state.value.childQuery.isNotBlank()) {
            tryToCall(
                block = { registerRepository.searchMakhdoom(state.value.childQuery) },
                onSuccess = { res ->
                    if (res != null) updateState {
                        copy(
                            selectedChildren = selectedChildren + res,
                            childQuery = ""
                        )
                    }
                },
                onError = {
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_search_child),
                        isSuccess = false
                    )
                }
            )
        }
    }

    override fun onRemoveChild(child: UserSummary) {
        updateState { copy(selectedChildren = selectedChildren - child) }
    }

    // Upload & Bottom Sheet
    override fun onClickUpload(target: UploadTarget) {
        updateState { copy(isUploadBottomSheetVisible = true, activeUploadTarget = target) }
    }

    override fun onDismissUploadBottomSheet() {
        updateState { copy(isUploadBottomSheetVisible = false, activeUploadTarget = null) }
    }

    fun onFileOptionPicked(option: FilePickOption) {
        val target = state.value.activeUploadTarget ?: return
        viewModelScope.launch {
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

    // Step 5: Verify
    private fun initiateWhatsAppStep5() {
        tryToCall(
            block = { registerRepository.initiateWhatsAppVerification(state.value.phone) },
            onSuccess = { res ->
                whatsappToken = res.token
                updateState { copy(whatsAppDeepLink = res.deepLink, currentStep = 5) }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_verify_whatsapp),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }

    override fun onClickVerifyWhatsApp() {
        // Handled in UI layer via LocalUriHandler
    }

    override fun onClickCheckWhatsAppStatus() {
        val token = whatsappToken
        if (token != null) {
            tryToCall(
                onStart = { updateState { copy(actionButtonState = AppButtonState.Loading) } },
                block = {
                    registerRepository.getWhatsAppStatus(token)
                    resetTo(PendingApprovalRoute)
                },
                onSuccess = {},
                onError = { throwable ->
                    showSnackBar(
                        title = UiText.StringRes(Res.string.failed_to_verify_whatsapp),
                        message = getLocalizedErrorMessage(throwable),
                        isSuccess = false
                    )
                },
                onEnd = { updateState { copy(actionButtonState = AppButtonState.Enabled) } }
            )
        }
    }
}
