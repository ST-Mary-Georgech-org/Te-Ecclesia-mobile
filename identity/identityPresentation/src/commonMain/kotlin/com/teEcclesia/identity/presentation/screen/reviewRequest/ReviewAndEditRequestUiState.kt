package com.teEcclesia.identity.presentation.screen.reviewRequest

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse

data class ReviewAndEditRequestUiState(
    val userId: String = "",
    val currentStep: Int = 1,
    val totalSteps: Int = 2,
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val userProfile: ProfileResponse? = null,
    val isRejectDialogVisible: Boolean = false,

    val code: String = "",
    val imageBytes: ByteArray? = null,
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
    val buildingNoError: UiText? = null,
    val streetError: UiText? = null,
    val areaError: UiText? = null,
    val floorError: UiText? = null,
    val specialMarkError: UiText? = null,

    val areas: List<String> = emptyList(),
    val isAreaSheetVisible: Boolean = false,
    val isAreaLoading: Boolean = false,
    val confessionPriests: List<Priest> = emptyList(),
    val selectedConfessionPriest: Priest? = null,
    val isPriestSheetVisible: Boolean = false,
    val isPriestLoading: Boolean = false,
    val isUploadBottomSheetVisible: Boolean = false,
    val activeUploadTarget: UploadTarget? = null,
    val ordinationCertificateBytes: ByteArray? = null,
    val ordinationCertificateFileName: String? = null,
    val identityCertificateBytes: ByteArray? = null,
    val identityCertificateFileName: String? = null,

    val selectedRole: UserRole = UserRole.MAKHDOOM,
    val isRoleEditable: Boolean = true,
    val roles: List<UserRole> = listOf(UserRole.MAKHDOOM, UserRole.KHADEM, UserRole.PARENT, UserRole.KAHEN),
    val isRoleSheetVisible: Boolean = false,
    val isMale: Boolean? = true,

    val shamamsaStatus: ShamamsaStudyStatus = ShamamsaStudyStatus.NO,

    val isOrdained: Boolean = false,
    val selectedRank: LookupResponse? = null,
    val ranks: List<LookupResponse> = emptyList(),
    val isRankSheetVisible: Boolean = false,
    val rankError: UiText? = null,
    val isOrdainedInThisChurch: Boolean = false,
    val ordinationYear: String = "",
    val ordinationYearError: UiText? = null,
    val bishopName: String = "",
    val ordinationPlace: String = "",

    val studentEducationalStage: LookupResponse? = null,
    val educationalStages: List<LookupResponse> = emptyList(),
    val isStageSheetVisible: Boolean = false,
    val stageError: UiText? = null,
    val studentEducationalYear: LookupResponse? = null,
    val educationalYears: List<LookupResponse> = emptyList(),
    val isYearSheetVisible: Boolean = false,
    val yearError: UiText? = null,
    val isFatherDeceased: Boolean = false,
    val fatherPhone: String = "",
    val fatherPhoneError: UiText? = null,
    val fatherWhatsapp: String = "",
    val fatherWhatsappError: UiText? = null,
    val isMotherDeceased: Boolean = false,
    val motherPhone: String = "",
    val motherPhoneError: UiText? = null,
    val motherWhatsapp: String = "",
    val motherWhatsappError: UiText? = null,

    val servantEducationalStages: List<LookupResponse> = emptyList(),
    val servantEducationalYears: List<LookupResponse> = emptyList(),
    val isServantStageSheetVisible: Boolean = false,
    val isServantYearSheetVisible: Boolean = false,
    val canApproveNewRequests: Boolean = false,
    val responsibleStages: List<LookupResponse> = emptyList(),
    val responsibleYears: List<LookupResponse> = emptyList(),
    val isResponsibleStageSheetVisible: Boolean = false,
    val isResponsibleYearSheetVisible: Boolean = false,

    val partnerQuery: String = "",
    val selectedPartner: UserSummary? = null,
    val childQuery: String = "",
    val selectedChildren: List<UserSummary> = emptyList(),

    val kahenEducationalStages: List<LookupResponse> = emptyList(),
    val isStagesSheetVisible: Boolean = false,
    val stagesError: UiText? = null,

    val notes: String = "",
    val notesError: UiText? = null,
    val submittedAt: String = "2026-02-16 13:06"
) {
    val progress: Float
        get() = currentStep.toFloat() / totalSteps.toFloat()

    val canGoPrevious: Boolean
        get() = currentStep > 1

    val canGoNext: Boolean
        get() = currentStep < totalSteps

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ReviewAndEditRequestUiState

        if (currentStep != other.currentStep) return false
        if (totalSteps != other.totalSteps) return false
        if (isLoading != other.isLoading) return false
        if (isSubmitting != other.isSubmitting) return false
        if (isRejectDialogVisible != other.isRejectDialogVisible) return false
        if (isFromAnotherChurch != other.isFromAnotherChurch) return false
        if (isAreaSheetVisible != other.isAreaSheetVisible) return false
        if (isAreaLoading != other.isAreaLoading) return false
        if (isPriestSheetVisible != other.isPriestSheetVisible) return false
        if (isPriestLoading != other.isPriestLoading) return false
        if (isUploadBottomSheetVisible != other.isUploadBottomSheetVisible) return false
        if (isRoleEditable != other.isRoleEditable) return false
        if (isRoleSheetVisible != other.isRoleSheetVisible) return false
        if (isMale != other.isMale) return false
        if (isOrdained != other.isOrdained) return false
        if (isRankSheetVisible != other.isRankSheetVisible) return false
        if (isOrdainedInThisChurch != other.isOrdainedInThisChurch) return false
        if (isStageSheetVisible != other.isStageSheetVisible) return false
        if (isYearSheetVisible != other.isYearSheetVisible) return false
        if (isFatherDeceased != other.isFatherDeceased) return false
        if (isMotherDeceased != other.isMotherDeceased) return false
        if (isServantStageSheetVisible != other.isServantStageSheetVisible) return false
        if (isServantYearSheetVisible != other.isServantYearSheetVisible) return false
        if (canApproveNewRequests != other.canApproveNewRequests) return false
        if (isResponsibleStageSheetVisible != other.isResponsibleStageSheetVisible) return false
        if (isResponsibleYearSheetVisible != other.isResponsibleYearSheetVisible) return false
        if (isStagesSheetVisible != other.isStagesSheetVisible) return false
        if (userId != other.userId) return false
        if (userProfile != other.userProfile) return false
        if (code != other.code) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (imageUrl != other.imageUrl) return false
        if (firstName != other.firstName) return false
        if (secondName != other.secondName) return false
        if (thirdName != other.thirdName) return false
        if (lastName != other.lastName) return false
        if (displayName != other.displayName) return false
        if (nationalId != other.nationalId) return false
        if (job != other.job) return false
        if (confessionPriestId != other.confessionPriestId) return false
        if (confessionPriestName != other.confessionPriestName) return false
        if (confessionPriestChurch != other.confessionPriestChurch) return false
        if (confessionPriestPhone != other.confessionPriestPhone) return false
        if (phone != other.phone) return false
        if (homePhone != other.homePhone) return false
        if (email != other.email) return false
        if (buildingNo != other.buildingNo) return false
        if (street != other.street) return false
        if (streetBranch != other.streetBranch) return false
        if (area != other.area) return false
        if (floor != other.floor) return false
        if (apartment != other.apartment) return false
        if (specialMark != other.specialMark) return false
        if (codeError != other.codeError) return false
        if (firstNameError != other.firstNameError) return false
        if (secondNameError != other.secondNameError) return false
        if (thirdNameError != other.thirdNameError) return false
        if (lastNameError != other.lastNameError) return false
        if (displayNameError != other.displayNameError) return false
        if (nationalIdError != other.nationalIdError) return false
        if (confessionPriestError != other.confessionPriestError) return false
        if (externalPriestNameError != other.externalPriestNameError) return false
        if (externalPriestChurchError != other.externalPriestChurchError) return false
        if (externalPriestPhoneError != other.externalPriestPhoneError) return false
        if (phoneError != other.phoneError) return false
        if (homePhoneError != other.homePhoneError) return false
        if (emailError != other.emailError) return false
        if (buildingNoError != other.buildingNoError) return false
        if (streetError != other.streetError) return false
        if (areaError != other.areaError) return false
        if (floorError != other.floorError) return false
        if (specialMarkError != other.specialMarkError) return false
        if (areas != other.areas) return false
        if (confessionPriests != other.confessionPriests) return false
        if (selectedConfessionPriest != other.selectedConfessionPriest) return false
        if (activeUploadTarget != other.activeUploadTarget) return false
        if (!ordinationCertificateBytes.contentEquals(other.ordinationCertificateBytes)) return false
        if (ordinationCertificateFileName != other.ordinationCertificateFileName) return false
        if (!identityCertificateBytes.contentEquals(other.identityCertificateBytes)) return false
        if (identityCertificateFileName != other.identityCertificateFileName) return false
        if (selectedRole != other.selectedRole) return false
        if (roles != other.roles) return false
        if (shamamsaStatus != other.shamamsaStatus) return false
        if (selectedRank != other.selectedRank) return false
        if (ranks != other.ranks) return false
        if (rankError != other.rankError) return false
        if (ordinationYear != other.ordinationYear) return false
        if (ordinationYearError != other.ordinationYearError) return false
        if (bishopName != other.bishopName) return false
        if (ordinationPlace != other.ordinationPlace) return false
        if (studentEducationalStage != other.studentEducationalStage) return false
        if (educationalStages != other.educationalStages) return false
        if (stageError != other.stageError) return false
        if (studentEducationalYear != other.studentEducationalYear) return false
        if (educationalYears != other.educationalYears) return false
        if (yearError != other.yearError) return false
        if (fatherPhone != other.fatherPhone) return false
        if (fatherPhoneError != other.fatherPhoneError) return false
        if (fatherWhatsapp != other.fatherWhatsapp) return false
        if (fatherWhatsappError != other.fatherWhatsappError) return false
        if (motherPhone != other.motherPhone) return false
        if (motherPhoneError != other.motherPhoneError) return false
        if (motherWhatsapp != other.motherWhatsapp) return false
        if (motherWhatsappError != other.motherWhatsappError) return false
        if (servantEducationalStages != other.servantEducationalStages) return false
        if (servantEducationalYears != other.servantEducationalYears) return false
        if (responsibleStages != other.responsibleStages) return false
        if (responsibleYears != other.responsibleYears) return false
        if (partnerQuery != other.partnerQuery) return false
        if (selectedPartner != other.selectedPartner) return false
        if (childQuery != other.childQuery) return false
        if (selectedChildren != other.selectedChildren) return false
        if (kahenEducationalStages != other.kahenEducationalStages) return false
        if (stagesError != other.stagesError) return false
        if (notes != other.notes) return false
        if (notesError != other.notesError) return false
        if (submittedAt != other.submittedAt) return false
        if (progress != other.progress) return false
        if (canGoPrevious != other.canGoPrevious) return false
        if (canGoNext != other.canGoNext) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentStep
        result = 31 * result + totalSteps
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isSubmitting.hashCode()
        result = 31 * result + isRejectDialogVisible.hashCode()
        result = 31 * result + isFromAnotherChurch.hashCode()
        result = 31 * result + isAreaSheetVisible.hashCode()
        result = 31 * result + isAreaLoading.hashCode()
        result = 31 * result + isPriestSheetVisible.hashCode()
        result = 31 * result + isPriestLoading.hashCode()
        result = 31 * result + isUploadBottomSheetVisible.hashCode()
        result = 31 * result + isRoleEditable.hashCode()
        result = 31 * result + isRoleSheetVisible.hashCode()
        result = 31 * result + (isMale?.hashCode() ?: 0)
        result = 31 * result + isOrdained.hashCode()
        result = 31 * result + isRankSheetVisible.hashCode()
        result = 31 * result + isOrdainedInThisChurch.hashCode()
        result = 31 * result + isStageSheetVisible.hashCode()
        result = 31 * result + isYearSheetVisible.hashCode()
        result = 31 * result + isFatherDeceased.hashCode()
        result = 31 * result + isMotherDeceased.hashCode()
        result = 31 * result + isServantStageSheetVisible.hashCode()
        result = 31 * result + isServantYearSheetVisible.hashCode()
        result = 31 * result + canApproveNewRequests.hashCode()
        result = 31 * result + isResponsibleStageSheetVisible.hashCode()
        result = 31 * result + isResponsibleYearSheetVisible.hashCode()
        result = 31 * result + isStagesSheetVisible.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (userProfile?.hashCode() ?: 0)
        result = 31 * result + code.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + firstName.hashCode()
        result = 31 * result + secondName.hashCode()
        result = 31 * result + thirdName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + nationalId.hashCode()
        result = 31 * result + job.hashCode()
        result = 31 * result + (confessionPriestId?.hashCode() ?: 0)
        result = 31 * result + confessionPriestName.hashCode()
        result = 31 * result + confessionPriestChurch.hashCode()
        result = 31 * result + confessionPriestPhone.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + homePhone.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + buildingNo.hashCode()
        result = 31 * result + street.hashCode()
        result = 31 * result + streetBranch.hashCode()
        result = 31 * result + area.hashCode()
        result = 31 * result + floor.hashCode()
        result = 31 * result + apartment.hashCode()
        result = 31 * result + specialMark.hashCode()
        result = 31 * result + (codeError?.hashCode() ?: 0)
        result = 31 * result + (firstNameError?.hashCode() ?: 0)
        result = 31 * result + (secondNameError?.hashCode() ?: 0)
        result = 31 * result + (thirdNameError?.hashCode() ?: 0)
        result = 31 * result + (lastNameError?.hashCode() ?: 0)
        result = 31 * result + (displayNameError?.hashCode() ?: 0)
        result = 31 * result + (nationalIdError?.hashCode() ?: 0)
        result = 31 * result + (confessionPriestError?.hashCode() ?: 0)
        result = 31 * result + (externalPriestNameError?.hashCode() ?: 0)
        result = 31 * result + (externalPriestChurchError?.hashCode() ?: 0)
        result = 31 * result + (externalPriestPhoneError?.hashCode() ?: 0)
        result = 31 * result + (phoneError?.hashCode() ?: 0)
        result = 31 * result + (homePhoneError?.hashCode() ?: 0)
        result = 31 * result + (emailError?.hashCode() ?: 0)
        result = 31 * result + (buildingNoError?.hashCode() ?: 0)
        result = 31 * result + (streetError?.hashCode() ?: 0)
        result = 31 * result + (areaError?.hashCode() ?: 0)
        result = 31 * result + (floorError?.hashCode() ?: 0)
        result = 31 * result + (specialMarkError?.hashCode() ?: 0)
        result = 31 * result + areas.hashCode()
        result = 31 * result + confessionPriests.hashCode()
        result = 31 * result + (selectedConfessionPriest?.hashCode() ?: 0)
        result = 31 * result + (activeUploadTarget?.hashCode() ?: 0)
        result = 31 * result + (ordinationCertificateBytes?.contentHashCode() ?: 0)
        result = 31 * result + (ordinationCertificateFileName?.hashCode() ?: 0)
        result = 31 * result + (identityCertificateBytes?.contentHashCode() ?: 0)
        result = 31 * result + (identityCertificateFileName?.hashCode() ?: 0)
        result = 31 * result + selectedRole.hashCode()
        result = 31 * result + roles.hashCode()
        result = 31 * result + shamamsaStatus.hashCode()
        result = 31 * result + (selectedRank?.hashCode() ?: 0)
        result = 31 * result + ranks.hashCode()
        result = 31 * result + (rankError?.hashCode() ?: 0)
        result = 31 * result + ordinationYear.hashCode()
        result = 31 * result + (ordinationYearError?.hashCode() ?: 0)
        result = 31 * result + bishopName.hashCode()
        result = 31 * result + ordinationPlace.hashCode()
        result = 31 * result + (studentEducationalStage?.hashCode() ?: 0)
        result = 31 * result + educationalStages.hashCode()
        result = 31 * result + (stageError?.hashCode() ?: 0)
        result = 31 * result + (studentEducationalYear?.hashCode() ?: 0)
        result = 31 * result + educationalYears.hashCode()
        result = 31 * result + (yearError?.hashCode() ?: 0)
        result = 31 * result + fatherPhone.hashCode()
        result = 31 * result + (fatherPhoneError?.hashCode() ?: 0)
        result = 31 * result + fatherWhatsapp.hashCode()
        result = 31 * result + (fatherWhatsappError?.hashCode() ?: 0)
        result = 31 * result + motherPhone.hashCode()
        result = 31 * result + (motherPhoneError?.hashCode() ?: 0)
        result = 31 * result + motherWhatsapp.hashCode()
        result = 31 * result + (motherWhatsappError?.hashCode() ?: 0)
        result = 31 * result + servantEducationalStages.hashCode()
        result = 31 * result + servantEducationalYears.hashCode()
        result = 31 * result + responsibleStages.hashCode()
        result = 31 * result + responsibleYears.hashCode()
        result = 31 * result + partnerQuery.hashCode()
        result = 31 * result + (selectedPartner?.hashCode() ?: 0)
        result = 31 * result + childQuery.hashCode()
        result = 31 * result + selectedChildren.hashCode()
        result = 31 * result + kahenEducationalStages.hashCode()
        result = 31 * result + (stagesError?.hashCode() ?: 0)
        result = 31 * result + notes.hashCode()
        result = 31 * result + (notesError?.hashCode() ?: 0)
        result = 31 * result + submittedAt.hashCode()
        result = 31 * result + progress.hashCode()
        result = 31 * result + canGoPrevious.hashCode()
        result = 31 * result + canGoNext.hashCode()
        return result
    }
}