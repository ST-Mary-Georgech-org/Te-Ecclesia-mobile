package com.teEcclesia.identity.presentation.screen.register

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.lookups.domain.model.LookupResponse

data class RegisterScreenState(
    val currentStep: Int = 1,
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val isLoading: Boolean = false,
    
    // Step 1: Personal Info
    val imageBytes: ByteArray? = null,
    val imageUrl: String? = null,
    val firstName: String = "",
    val firstNameError: UiText? = null,
    val secondName: String = "",
    val secondNameError: UiText? = null,
    val thirdName: String = "",
    val thirdNameError: UiText? = null,
    val lastName: String = "",
    val lastNameError: UiText? = null,
    val displayName: String = "",
    val displayNameError: UiText? = null,
    val nationalId: String = "",
    val nationalIdError: UiText? = null,
    val isMale: Boolean? = null,
    val job: String = "",

    val confessionPriests: List<Priest> = emptyList(),
    val isPriestLoading: Boolean = false,
    val isPriestEndReached: Boolean = false,
    val selectedConfessionPriest: Priest? = null,
    val confessionPriestError: UiText? = null,
    val isFromAnotherChurch: Boolean = false,
    val externalPriestName: String = "",
    val externalPriestNameError: UiText? = null,
    val externalPriestChurch: String = "",
    val externalPriestChurchError: UiText? = null,
    val externalPriestPhone: String = "",
    val externalPriestPhoneError: UiText? = null,
    val isPriestSheetVisible: Boolean = false,

    // Step 2: Contact & Address
    val phone: String = "",
    val phoneError: UiText? = null,
    val homePhone: String = "",
    val homePhoneError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,

    val buildingNo: String = "",
    val buildingNoError: UiText? = null,
    val street: String = "",
    val streetError: UiText? = null,
    val streetBranch: String = "",
    val areas: List<String> = emptyList(),
    val isAreaLoading: Boolean = false,
    val selectedArea: String? = null,
    val areaError: UiText? = null,
    val isAreaSheetVisible: Boolean = false,
    val floor: String = "",
    val floorError: UiText? = null,
    val apartment: String = "",
    val specialMark: String = "",
    val specialMarkError: UiText? = null,

    // Step 3: Choose Role
    val selectedRole: UserRole? = UserRole.MAKHDOOM,

    // Step 4: Role Profiles
    // Ordination Info
    val isOrdained: Boolean = true,
    val ranks: List<LookupResponse> = emptyList(),
    val isRankLoading: Boolean = false,
    val isRankEndReached: Boolean = false,
    val selectedRank: LookupResponse? = null,
    val rankError: UiText? = null,
    val isRankSheetVisible: Boolean = false,
    val isOrdainedInThisChurch: Boolean = true,
    val ordinationYear: String = "",
    val ordinationYearError: UiText? = null,
    val bishopName: String = "",
    val ordinationPlace: String = "",
    val ordinationCertificateBytes: ByteArray? = null,
    val ordinationCertificateFileName: String? = null,

    // Deacons School / Student, Servant & Kahen Info
    val shamamsaStatus: ShamamsaStudyStatus = ShamamsaStudyStatus.YES,
    val educationalStages: List<LookupResponse> = emptyList(),
    val isStageLoading: Boolean = false,
    val isStageEndReached: Boolean = false,
    
    // Student (Makhdoom)
    val studentEducationalStage: LookupResponse? = null,
    val studentEducationalYear: LookupResponse? = null,
    
    // Servant (Khadem)
    val servantEducationalStage: LookupResponse? = null,
    val servantEducationalYear: LookupResponse? = null,

    // Priest (Kahen)
    val kahenEducationalStages: List<LookupResponse> = emptyList(),
    
    val stagesError: UiText? = null,
    val isStagesSheetVisible: Boolean = false,
    val stageError: UiText? = null,
    val isStageSheetVisible: Boolean = false,
    val yearError: UiText? = null,
    val isYearSheetVisible: Boolean = false,

    // Deceased Checks
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
    val identityCertificateBytes: ByteArray? = null,
    val identityCertificateFileName: String? = null,

    // Parent Info
    val partnerQuery: String = "",
    val selectedPartner: UserSummary? = null,
    val childQuery: String = "",
    val selectedChildren: List<UserSummary> = emptyList(),

    // Upload Bottom Sheet state
    val isUploadBottomSheetVisible: Boolean = false,
    val activeUploadTarget: UploadTarget? = null,
    
    // Step 5: WhatsApp Verify Link
    val whatsAppDeepLink: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RegisterScreenState

        if (currentStep != other.currentStep) return false
        if (isLoading != other.isLoading) return false
        if (isPriestLoading != other.isPriestLoading) return false
        if (isPriestEndReached != other.isPriestEndReached) return false
        if (isFromAnotherChurch != other.isFromAnotherChurch) return false
        if (isPriestSheetVisible != other.isPriestSheetVisible) return false
        if (isPasswordVisible != other.isPasswordVisible) return false
        if (isAreaLoading != other.isAreaLoading) return false
        if (isAreaSheetVisible != other.isAreaSheetVisible) return false
        if (isOrdained != other.isOrdained) return false
        if (isRankLoading != other.isRankLoading) return false
        if (isRankEndReached != other.isRankEndReached) return false
        if (isRankSheetVisible != other.isRankSheetVisible) return false
        if (isOrdainedInThisChurch != other.isOrdainedInThisChurch) return false
        if (isStageLoading != other.isStageLoading) return false
        if (isStageEndReached != other.isStageEndReached) return false
        if (isStagesSheetVisible != other.isStagesSheetVisible) return false
        if (isStageSheetVisible != other.isStageSheetVisible) return false
        if (isYearSheetVisible != other.isYearSheetVisible) return false
        if (isFatherDeceased != other.isFatherDeceased) return false
        if (isMotherDeceased != other.isMotherDeceased) return false
        if (isUploadBottomSheetVisible != other.isUploadBottomSheetVisible) return false
        if (actionButtonState != other.actionButtonState) return false
        if (!imageBytes.contentEquals(other.imageBytes)) return false
        if (imageUrl != other.imageUrl) return false
        if (firstName != other.firstName) return false
        if (firstNameError != other.firstNameError) return false
        if (secondName != other.secondName) return false
        if (secondNameError != other.secondNameError) return false
        if (thirdName != other.thirdName) return false
        if (thirdNameError != other.thirdNameError) return false
        if (lastName != other.lastName) return false
        if (lastNameError != other.lastNameError) return false
        if (displayName != other.displayName) return false
        if (displayNameError != other.displayNameError) return false
        if (nationalId != other.nationalId) return false
        if (nationalIdError != other.nationalIdError) return false
        if (isMale != other.isMale) return false
        if (job != other.job) return false
        if (confessionPriests != other.confessionPriests) return false
        if (selectedConfessionPriest != other.selectedConfessionPriest) return false
        if (confessionPriestError != other.confessionPriestError) return false
        if (externalPriestName != other.externalPriestName) return false
        if (externalPriestNameError != other.externalPriestNameError) return false
        if (externalPriestChurch != other.externalPriestChurch) return false
        if (externalPriestChurchError != other.externalPriestChurchError) return false
        if (externalPriestPhone != other.externalPriestPhone) return false
        if (externalPriestPhoneError != other.externalPriestPhoneError) return false
        if (phone != other.phone) return false
        if (phoneError != other.phoneError) return false
        if (homePhone != other.homePhone) return false
        if (homePhoneError != other.homePhoneError) return false
        if (email != other.email) return false
        if (emailError != other.emailError) return false
        if (password != other.password) return false
        if (passwordError != other.passwordError) return false
        if (buildingNo != other.buildingNo) return false
        if (buildingNoError != other.buildingNoError) return false
        if (street != other.street) return false
        if (streetError != other.streetError) return false
        if (streetBranch != other.streetBranch) return false
        if (areas != other.areas) return false
        if (selectedArea != other.selectedArea) return false
        if (areaError != other.areaError) return false
        if (floor != other.floor) return false
        if (floorError != other.floorError) return false
        if (apartment != other.apartment) return false
        if (specialMark != other.specialMark) return false
        if (specialMarkError != other.specialMarkError) return false
        if (selectedRole != other.selectedRole) return false
        if (ranks != other.ranks) return false
        if (selectedRank != other.selectedRank) return false
        if (rankError != other.rankError) return false
        if (ordinationYear != other.ordinationYear) return false
        if (ordinationYearError != other.ordinationYearError) return false
        if (bishopName != other.bishopName) return false
        if (ordinationPlace != other.ordinationPlace) return false
        if (!ordinationCertificateBytes.contentEquals(other.ordinationCertificateBytes)) return false
        if (ordinationCertificateFileName != other.ordinationCertificateFileName) return false
        if (shamamsaStatus != other.shamamsaStatus) return false
        if (educationalStages != other.educationalStages) return false
        if (studentEducationalStage != other.studentEducationalStage) return false
        if (studentEducationalYear != other.studentEducationalYear) return false
        if (servantEducationalStage != other.servantEducationalStage) return false
        if (servantEducationalYear != other.servantEducationalYear) return false
        if (kahenEducationalStages != other.kahenEducationalStages) return false
        if (stagesError != other.stagesError) return false
        if (stageError != other.stageError) return false
        if (yearError != other.yearError) return false
        if (fatherPhone != other.fatherPhone) return false
        if (fatherPhoneError != other.fatherPhoneError) return false
        if (fatherWhatsapp != other.fatherWhatsapp) return false
        if (fatherWhatsappError != other.fatherWhatsappError) return false
        if (motherPhone != other.motherPhone) return false
        if (motherPhoneError != other.motherPhoneError) return false
        if (motherWhatsapp != other.motherWhatsapp) return false
        if (motherWhatsappError != other.motherWhatsappError) return false
        if (!identityCertificateBytes.contentEquals(other.identityCertificateBytes)) return false
        if (identityCertificateFileName != other.identityCertificateFileName) return false
        if (partnerQuery != other.partnerQuery) return false
        if (selectedPartner != other.selectedPartner) return false
        if (childQuery != other.childQuery) return false
        if (selectedChildren != other.selectedChildren) return false
        if (activeUploadTarget != other.activeUploadTarget) return false
        if (whatsAppDeepLink != other.whatsAppDeepLink) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentStep
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isPriestLoading.hashCode()
        result = 31 * result + isPriestEndReached.hashCode()
        result = 31 * result + isFromAnotherChurch.hashCode()
        result = 31 * result + isPriestSheetVisible.hashCode()
        result = 31 * result + isPasswordVisible.hashCode()
        result = 31 * result + isAreaLoading.hashCode()
        result = 31 * result + isAreaSheetVisible.hashCode()
        result = 31 * result + isOrdained.hashCode()
        result = 31 * result + isRankLoading.hashCode()
        result = 31 * result + isRankEndReached.hashCode()
        result = 31 * result + isRankSheetVisible.hashCode()
        result = 31 * result + isOrdainedInThisChurch.hashCode()
        result = 31 * result + isStageLoading.hashCode()
        result = 31 * result + isStageEndReached.hashCode()
        result = 31 * result + isStagesSheetVisible.hashCode()
        result = 31 * result + isStageSheetVisible.hashCode()
        result = 31 * result + isYearSheetVisible.hashCode()
        result = 31 * result + isFatherDeceased.hashCode()
        result = 31 * result + isMotherDeceased.hashCode()
        result = 31 * result + isUploadBottomSheetVisible.hashCode()
        result = 31 * result + actionButtonState.hashCode()
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + firstName.hashCode()
        result = 31 * result + (firstNameError?.hashCode() ?: 0)
        result = 31 * result + secondName.hashCode()
        result = 31 * result + (secondNameError?.hashCode() ?: 0)
        result = 31 * result + thirdName.hashCode()
        result = 31 * result + (thirdNameError?.hashCode() ?: 0)
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (lastNameError?.hashCode() ?: 0)
        result = 31 * result + displayName.hashCode()
        result = 31 * result + (displayNameError?.hashCode() ?: 0)
        result = 31 * result + nationalId.hashCode()
        result = 31 * result + (nationalIdError?.hashCode() ?: 0)
        result = 31 * result + (isMale?.hashCode() ?: 0)
        result = 31 * result + job.hashCode()
        result = 31 * result + confessionPriests.hashCode()
        result = 31 * result + (selectedConfessionPriest?.hashCode() ?: 0)
        result = 31 * result + (confessionPriestError?.hashCode() ?: 0)
        result = 31 * result + externalPriestName.hashCode()
        result = 31 * result + (externalPriestNameError?.hashCode() ?: 0)
        result = 31 * result + externalPriestChurch.hashCode()
        result = 31 * result + (externalPriestChurchError?.hashCode() ?: 0)
        result = 31 * result + externalPriestPhone.hashCode()
        result = 31 * result + (externalPriestPhoneError?.hashCode() ?: 0)
        result = 31 * result + phone.hashCode()
        result = 31 * result + (phoneError?.hashCode() ?: 0)
        result = 31 * result + homePhone.hashCode()
        result = 31 * result + (homePhoneError?.hashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + (emailError?.hashCode() ?: 0)
        result = 31 * result + password.hashCode()
        result = 31 * result + (passwordError?.hashCode() ?: 0)
        result = 31 * result + buildingNo.hashCode()
        result = 31 * result + (buildingNoError?.hashCode() ?: 0)
        result = 31 * result + street.hashCode()
        result = 31 * result + (streetError?.hashCode() ?: 0)
        result = 31 * result + streetBranch.hashCode()
        result = 31 * result + areas.hashCode()
        result = 31 * result + (selectedArea?.hashCode() ?: 0)
        result = 31 * result + (areaError?.hashCode() ?: 0)
        result = 31 * result + floor.hashCode()
        result = 31 * result + (floorError?.hashCode() ?: 0)
        result = 31 * result + apartment.hashCode()
        result = 31 * result + specialMark.hashCode()
        result = 31 * result + (specialMarkError?.hashCode() ?: 0)
        result = 31 * result + (selectedRole?.hashCode() ?: 0)
        result = 31 * result + ranks.hashCode()
        result = 31 * result + (selectedRank?.hashCode() ?: 0)
        result = 31 * result + (rankError?.hashCode() ?: 0)
        result = 31 * result + ordinationYear.hashCode()
        result = 31 * result + (ordinationYearError?.hashCode() ?: 0)
        result = 31 * result + bishopName.hashCode()
        result = 31 * result + ordinationPlace.hashCode()
        result = 31 * result + (ordinationCertificateBytes?.contentHashCode() ?: 0)
        result = 31 * result + (ordinationCertificateFileName?.hashCode() ?: 0)
        result = 31 * result + shamamsaStatus.hashCode()
        result = 31 * result + educationalStages.hashCode()
        result = 31 * result + (studentEducationalStage?.hashCode() ?: 0)
        result = 31 * result + (studentEducationalYear?.hashCode() ?: 0)
        result = 31 * result + (servantEducationalStage?.hashCode() ?: 0)
        result = 31 * result + (servantEducationalYear?.hashCode() ?: 0)
        result = 31 * result + kahenEducationalStages.hashCode()
        result = 31 * result + (stagesError?.hashCode() ?: 0)
        result = 31 * result + (stageError?.hashCode() ?: 0)
        result = 31 * result + (yearError?.hashCode() ?: 0)
        result = 31 * result + fatherPhone.hashCode()
        result = 31 * result + (fatherPhoneError?.hashCode() ?: 0)
        result = 31 * result + fatherWhatsapp.hashCode()
        result = 31 * result + (fatherWhatsappError?.hashCode() ?: 0)
        result = 31 * result + motherPhone.hashCode()
        result = 31 * result + (motherPhoneError?.hashCode() ?: 0)
        result = 31 * result + motherWhatsapp.hashCode()
        result = 31 * result + (motherWhatsappError?.hashCode() ?: 0)
        result = 31 * result + (identityCertificateBytes?.contentHashCode() ?: 0)
        result = 31 * result + (identityCertificateFileName?.hashCode() ?: 0)
        result = 31 * result + partnerQuery.hashCode()
        result = 31 * result + (selectedPartner?.hashCode() ?: 0)
        result = 31 * result + childQuery.hashCode()
        result = 31 * result + selectedChildren.hashCode()
        result = 31 * result + (activeUploadTarget?.hashCode() ?: 0)
        result = 31 * result + (whatsAppDeepLink?.hashCode() ?: 0)
        return result
    }
}

enum class UploadTarget {
    PROFILE_PHOTO,
    ORDINATION_CERTIFICATE,
    IDENTITY_CERTIFICATE
}
