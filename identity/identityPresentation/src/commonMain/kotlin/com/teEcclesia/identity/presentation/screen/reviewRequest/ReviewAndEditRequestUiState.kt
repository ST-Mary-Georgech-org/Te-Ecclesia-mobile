package com.teEcclesia.identity.presentation.screen.reviewRequest

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.model.SafeByteArray
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.domain.model.DeaconsSchoolStatus
import com.teEcclesia.identity.domain.model.DeaconsSchoolRecordRequest
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.OrdinationProfileRequest
import com.teEcclesia.identity.domain.model.MakhdoomProfileRequest
import com.teEcclesia.identity.domain.model.KhademProfileRequest
import com.teEcclesia.identity.domain.model.ParentProfileRequest
import com.teEcclesia.identity.domain.model.KahenProfileRequest
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.action_approved_by
import teecclesia.designsystem.generated.resources.action_banned_by
import teecclesia.designsystem.generated.resources.action_rejected_by
import teecclesia.designsystem.generated.resources.action_updated_by
import teecclesia.designsystem.generated.resources.status_completed
import teecclesia.designsystem.generated.resources.status_incomplete
import teecclesia.designsystem.generated.resources.status_pending

data class ReviewAndEditRequestUiState(
    val userId: String = "",
    val currentStep: Int = 1,
    val totalSteps: Int = 2,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isReadOnlyMode: Boolean = false,
    val isSubmitting: Boolean = false,
    val userProfile: ProfileResponse? = null,
    val isRejectDialogVisible: Boolean = false,

    val code: String = "",
    val imageBytes: SafeByteArray? = null,
    val imageUrl: String? = null,
    val firstName: String = "",
    val secondName: String = "",
    val thirdName: String = "",
    val lastName: String = "",
    val displayName: String = "",
    val nationalId: String = "",
    val job: String = "",

    val isFromAnotherChurch: Boolean = false,
    val confessionPriestId: String? = null,
    val confessionPriestName: String = "",
    val confessionPriestChurch: String = "",
    val confessionPriestPhone: String = "",

    val phone: String = "",
    val homePhone: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isUpdateMode: Boolean = false,

    val buildingNo: String = "",
    val street: String = "",
    val streetBranch: String = "",
    val area: String = "",
    val floor: String = "",
    val apartment: String = "",
    val specialMark: String = "",

    val codeError: UiText? = null,
    val firstNameError: UiText? = null,
    val secondNameError: UiText? = null,
    val thirdNameError: UiText? = null,
    val lastNameError: UiText? = null,
    val displayNameError: UiText? = null,
    val nationalIdError: UiText? = null,
    val confessionPriestError: UiText? = null,
    val externalPriestNameError: UiText? = null,
    val externalPriestChurchError: UiText? = null,
    val externalPriestPhoneError: UiText? = null,
    val phoneError: UiText? = null,
    val homePhoneError: UiText? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val buildingNoError: UiText? = null,
    val streetError: UiText? = null,
    val areaError: UiText? = null,
    val floorError: UiText? = null,
    val specialMarkError: UiText? = null,

    val areas: List<String> = emptyList(),
    val isAreaSheetVisible: Boolean = false,
    val isAreaLoading: Boolean = false,
    val isAreaLoadFailed: Boolean = false,
    val confessionPriests: List<Priest> = emptyList(),
    val selectedConfessionPriest: Priest? = null,
    val isPriestSheetVisible: Boolean = false,
    val isPriestLoading: Boolean = false,
    val isPriestLoadFailed: Boolean = false,
    val isUploadBottomSheetVisible: Boolean = false,
    val isImageViewerVisible: Boolean = false,
    val activeImageViewerModel: Any? = null,
    val isPdfViewerVisible: Boolean = false,
    val activePdfBytes: SafeByteArray? = null,
    val activeUploadTarget: UploadTarget? = null,
    val ordinationCertificateBytes: SafeByteArray? = null,
    val ordinationCertificateFileName: String? = null,
    val identityCertificateBytes: SafeByteArray? = null,
    val identityCertificateFileName: String? = null,

    val selectedRole: UserRole = UserRole.MAKHDOOM,
    val isRoleEditable: Boolean = true,
    val roles: List<UserRole> = listOf(UserRole.MAKHDOOM, UserRole.KHADEM, UserRole.PARENT, UserRole.KAHEN),
    val isRoleSheetVisible: Boolean = false,
    val isMale: Boolean? = null,

    val shamamsaStatus: ShamamsaStudyStatus = ShamamsaStudyStatus.NO,

    val isOrdained: Boolean = false,
    val selectedRank: LookupResponse? = null,
    val ranks: List<LookupResponse> = emptyList(),
    val isRankLoading: Boolean = false,
    val isRankLoadFailed: Boolean = false,
    val isRankSheetVisible: Boolean = false,
    val rankError: UiText? = null,
    val isOrdainedInThisChurch: Boolean = false,
    val ordinationYear: String = "",
    val ordinationYearError: UiText? = null,
    val bishopName: String = "",
    val ordinationPlace: String = "",

    val studentEducationalStage: LookupResponse? = null,
    val educationalStages: List<LookupResponse> = emptyList(),
    val isStageLoading: Boolean = false,
    val isStageLoadFailed: Boolean = false,
    val isStageEndReached: Boolean = false,
    val isStageSheetVisible: Boolean = false,
    val stageError: UiText? = null,
    val studentEducationalYear: LookupResponse? = null,
    val educationalYears: List<LookupResponse> = emptyList(),
    val isYearLoading: Boolean = false,
    val isYearLoadFailed: Boolean = false,
    val isYearEndReached: Boolean = false,
    val isYearSheetVisible: Boolean = false,
    val yearError: UiText? = null,
    val isFatherDeceased: Boolean = false,
    val fatherPhone: String = "",
    val fatherPhoneError: UiText? = null,
    val fatherWhatsapp: String = "",
    val fatherWhatsappError: UiText? = null,
    val isFatherWhatsappSameAsPhone: Boolean = true,
    val isMotherDeceased: Boolean = false,
    val motherPhone: String = "",
    val motherPhoneError: UiText? = null,
    val motherWhatsapp: String = "",
    val motherWhatsappError: UiText? = null,
    val isMotherWhatsappSameAsPhone: Boolean = true,

    val servantEducationalStage: LookupResponse? = null,
    val isServantStageSheetVisible: Boolean = false,
    val isServantStageLoading: Boolean = false,
    val isServantStageLoadFailed: Boolean = false,
    val isServantStageEndReached: Boolean = false,
    val servantStageError: UiText? = null,
    val servantEducationalYear: LookupResponse? = null,
    val isServantYearSheetVisible: Boolean = false,
    val isServantYearLoading: Boolean = false,
    val isServantYearLoadFailed: Boolean = false,
    val isServantYearEndReached: Boolean = false,
    val servantYearError: UiText? = null,
    val canApproveNewRequests: Boolean = false,
    val canEditUser: Boolean = false,
    val responsibleStages: List<LookupResponse> = emptyList(),
    val isResponsibleStageSheetVisible: Boolean = false,
    val responsibleYears: List<LookupResponse> = emptyList(),
    val isResponsibleYearSheetVisible: Boolean = false,
    val allAvailableYearsForPermissions: List<LookupResponse> = emptyList(),
    val isResponsibleYearLoading: Boolean = false,
    val isResponsibleYearLoadFailed: Boolean = false,
    val isResponsibleYearEndReached: Boolean = false,
    val servantAvailableYears: List<LookupResponse> = emptyList(),

    val isPartnerLoading: Boolean = false,
    val partnerQuery: String = "",
    val selectedPartner: UserSummary? = null,
    val partnerError: UiText? = null,
    val isChildLoading: Boolean = false,
    val childQuery: String = "",
    val selectedChildren: List<UserSummary> = emptyList(),
    val childError: UiText? = null,
    val isAlsoParent: Boolean = false,

    val kahenEducationalStages: List<LookupResponse> = emptyList(),
    val stagesError: UiText? = null,
    val isStagesSheetVisible: Boolean = false,

    val notes: String = "",
    val notesError: UiText? = null,
    val submittedAt: String = "",

    val actionTakenAt: String? = null,
    val actionTakenByName: String = "",
    val actionTakenByUserCode: String = "",
    val isEnrolledInDeaconSchool: Boolean = false,
    val isDeaconSchoolPaid: Boolean = false,
    val deaconSchoolPaidAmount: String = "",
    val deaconSchoolStatus: DeaconsSchoolStatus = DeaconsSchoolStatus.PENDING,
    val isDeaconSchoolStatusSheetVisible: Boolean = false,
    val isAdmin: Boolean = false
) {
    val progress: Float
        get() = currentStep.toFloat() / totalSteps.toFloat()

    val canGoPrevious: Boolean
        get() = currentStep > 1

    val canGoNext: Boolean
        get() = currentStep < totalSteps

    val fullName: String
        get() = "$firstName $secondName $thirdName $lastName"
}

fun ReviewAndEditRequestUiState.toRegisterRequest(): RegisterRequest {
    return RegisterRequest(
        firstName = firstName.trim(),
        secondName = secondName.trim(),
        thirdName = thirdName.trim(),
        lastName = lastName.trim(),
        displayName = displayName.trim(),
        nationalId = nationalId.trim(),
        phone = phone.trim(),
        homePhone = homePhone.trim(),
        email = email.trim().ifBlank { null },
        password = password.ifBlank { null },
        imageUrl = imageUrl,
        identityDocumentImageUrl = identityCertificateFileName,
        job = job,
        buildingNo = buildingNo.trim(),
        street = street.trim(),
        streetBranch = streetBranch.ifBlank { null },
        area = area.trim(),
        floor = floor.trim(),
        apartment = apartment.ifBlank { null },
        specialMark = specialMark.trim(),
        role = selectedRole,
        confessionPriestId = if (!isFromAnotherChurch) selectedConfessionPriest?.id else null,
        externalConfessionPriestName = if (isFromAnotherChurch) confessionPriestName.ifBlank { null } else null,
        externalConfessionChurch = if (isFromAnotherChurch) confessionPriestChurch.ifBlank { null } else null,
        externalConfessionPhone = if (isFromAnotherChurch) confessionPriestPhone.ifBlank { null } else null,
        ordinationProfile = toOrdinationProfileRequest(),
        makhdoomProfile = toMakhdoomProfileRequest(),
        khademProfile = toKhademProfileRequest(),
        parentProfile = toParentProfileRequest(),
        kahenProfile = toKahenProfileRequest(),
        deaconsSchoolRecord = if (selectedRole == UserRole.MAKHDOOM && isAdmin) {
            DeaconsSchoolRecordRequest(
                enrolled = isEnrolledInDeaconSchool,
                paid = isDeaconSchoolPaid,
                paidAmount = deaconSchoolPaidAmount.toDoubleOrNull() ?: 0.0,
                status = deaconSchoolStatus
            )
        } else null
    )
}

private fun ReviewAndEditRequestUiState.toOrdinationProfileRequest(): OrdinationProfileRequest? {
    if (isMale == false || selectedRole == UserRole.KAHEN || selectedRole == UserRole.PARENT) return null
    return if (isOrdained) {
        OrdinationProfileRequest(
            rankId = selectedRank?.id ?: 1L,
            isOrdinationInAnotherChurch = !isOrdainedInThisChurch,
            ordinationYear = ordinationYear.toIntOrNull(),
            bishopName = bishopName.ifBlank { null },
            ordinationPlace = ordinationPlace.ifBlank { null }
        )
    } else userProfile?.ordinationProfile?.let { old ->
        OrdinationProfileRequest(
            rankId = old.rank.id,
            isOrdinationInAnotherChurch = old.isOrdinationInAnotherChurch,
            ordinationYear = old.ordinationYear,
            bishopName = old.bishopName.ifBlank { null },
            ordinationPlace = old.ordinationPlace.ifBlank { null },
            certificateImageUrl = old.certificateImageUrl
        )
    }
}

private fun ReviewAndEditRequestUiState.toMakhdoomProfileRequest(): MakhdoomProfileRequest? {
    return if (selectedRole == UserRole.MAKHDOOM) {
        MakhdoomProfileRequest(
            shamamsaStudyStatus = shamamsaStatus,
            educationalStageId = studentEducationalStage?.id ?: 1L,
            educationalYearId = studentEducationalYear?.id,
            isFatherDeceased = isFatherDeceased,
            fatherPhone = fatherPhone.ifBlank { null },
            fatherWhatsapp = (if (isFatherWhatsappSameAsPhone) fatherPhone else fatherWhatsapp).ifBlank { null },
            isMotherDeceased = isMotherDeceased,
            motherPhone = motherPhone.ifBlank { null },
            motherWhatsapp = (if (isMotherWhatsappSameAsPhone) motherPhone else motherWhatsapp).ifBlank { null }
        )
    } else userProfile?.makhdoomProfile?.let { old ->
        MakhdoomProfileRequest(
            shamamsaStudyStatus = old.shamamsaStudyStatus,
            educationalStageId = old.educationalStage.id,
            educationalYearId = old.educationalYear?.id,
            fatherPhone = old.fatherPhone.ifBlank { null },
            fatherWhatsapp = old.fatherWhatsapp.ifBlank { null },
            motherPhone = old.motherPhone.ifBlank { null },
            motherWhatsapp = old.motherWhatsapp.ifBlank { null },
            isFatherDeceased = old.isFatherDeceased,
            isMotherDeceased = old.isMotherDeceased
        )
    }
}

private fun ReviewAndEditRequestUiState.toKhademProfileRequest(): KhademProfileRequest? {
    return if (selectedRole == UserRole.KHADEM) {
        KhademProfileRequest(
            educationalStageId = servantEducationalStage?.id ?: 1L,
            educationalYearId = servantEducationalYear?.id,
            canApproveRequests = canApproveNewRequests,
            responsibleStageIds = responsibleStages.map { it.id },
            responsibleYearIds = responsibleYears.map { it.id }
        )
    } else userProfile?.khademProfile?.let { old ->
        KhademProfileRequest(
            educationalStageId = old.educationalStage.id,
            educationalYearId = old.educationalYear?.id,
            canApproveRequests = old.canApproveRequests,
            responsibleStageIds = old.responsibleStages.map { it.id },
            responsibleYearIds = old.responsibleYears.map { it.id }
        )
    }
}

private fun ReviewAndEditRequestUiState.toParentProfileRequest(): ParentProfileRequest? {
    val shouldInclude = selectedRole == UserRole.PARENT || ((selectedRole == UserRole.KHADEM || selectedRole == UserRole.KAHEN) && isAlsoParent)
    return if (shouldInclude) {
        ParentProfileRequest(
            partnerCode = selectedPartner?.code,
            childrenCodes = selectedChildren.mapNotNull { it.code }
        )
    } else userProfile?.parentProfile?.let { old ->
        ParentProfileRequest(
            partnerCode = old.partner?.code,
            childrenCodes = old.children.mapNotNull { it.code }
        )
    }
}

private fun ReviewAndEditRequestUiState.toKahenProfileRequest(): KahenProfileRequest? {
    return if (selectedRole == UserRole.KAHEN) {
        KahenProfileRequest(
            educationalStageIds = kahenEducationalStages.map { it.id }
        )
    } else userProfile?.kahenProfile?.let { old ->
        KahenProfileRequest(
            educationalStageIds = old.educationalStages.map { it.id }
        )
    }
}

fun DeaconsSchoolStatus.toUiText(): UiText = when (this) {
    DeaconsSchoolStatus.PENDING -> UiText.StringRes(Res.string.status_pending)
    DeaconsSchoolStatus.INCOMPLETE -> UiText.StringRes(Res.string.status_incomplete)
    DeaconsSchoolStatus.COMPLETED -> UiText.StringRes(Res.string.status_completed)
}

val ReviewAndEditRequestUiState.actionTypeLabel: UiText
    get() = when (userProfile?.status) {
        UserStatus.APPROVED -> UiText.StringRes(Res.string.action_approved_by)
        UserStatus.REJECTED -> UiText.StringRes(Res.string.action_rejected_by)
        UserStatus.BANNED -> UiText.StringRes(Res.string.action_banned_by)
        UserStatus.PENDING_APPROVAL,
        UserStatus.UNVERIFIED,
        UserStatus.PROFILE_INCOMPLETE,
        null -> UiText.StringRes(Res.string.action_updated_by)
    }
