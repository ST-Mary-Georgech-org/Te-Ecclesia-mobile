package com.teEcclesia.identity.presentation.screen.register

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.model.SafeByteArray
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.lookups.domain.model.LookupResponse

data class RegisterScreenState(
    val currentStep: Int = 1,
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    
    // Step 1: Personal Info
    val imageBytes: SafeByteArray? = null,
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
    val isPriestLoadFailed: Boolean = false,
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
    val isAreaLoadFailed: Boolean = false,
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
    val isRankLoadFailed: Boolean = false,
    val isRankEndReached: Boolean = false,
    val selectedRank: LookupResponse? = null,
    val rankError: UiText? = null,
    val isRankSheetVisible: Boolean = false,
    val isOrdainedInThisChurch: Boolean = true,
    val ordinationYear: String = "",
    val ordinationYearError: UiText? = null,
    val bishopName: String = "",
    val ordinationPlace: String = "",
    val ordinationCertificateBytes: SafeByteArray? = null,
    val ordinationCertificateFileName: String? = null,

    // Deacons School / Student, Servant & Kahen Info
    val shamamsaStatus: ShamamsaStudyStatus = ShamamsaStudyStatus.YES,
    val educationalStages: List<LookupResponse> = emptyList(),
    val isStageLoading: Boolean = false,
    val isStageLoadFailed: Boolean = false,
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
    val isFatherWhatsappSameAsPhone: Boolean = true,

    val isMotherDeceased: Boolean = false,
    val motherPhone: String = "",
    val motherPhoneError: UiText? = null,
    val motherWhatsapp: String = "",
    val motherWhatsappError: UiText? = null,
    val isMotherWhatsappSameAsPhone: Boolean = true,
    val identityCertificateBytes: SafeByteArray? = null,
    val identityCertificateFileName: String? = null,
    val identityCertificateError: UiText? = null,

    // Parent Info
    val isAlsoParent: Boolean = false,
    val isPartnerLoading: Boolean = false,
    val partnerQuery: String = "",
    val selectedPartner: UserSummary? = null,
    val partnerError: UiText? = null,
    val isChildLoading: Boolean = false,
    val childQuery: String = "",
    val selectedChildren: List<UserSummary> = emptyList(),
    val childError: UiText? = null,

    // Upload Bottom Sheet state
    val isUploadBottomSheetVisible: Boolean = false,
    val isImageViewerVisible: Boolean = false,
    val activeImageViewerModel: Any? = null,
    val isPdfViewerVisible: Boolean = false,
    val activePdfBytes: SafeByteArray? = null,
    val activeUploadTarget: UploadTarget? = null,
    
    // Step 5: WhatsApp Verify Link
    val whatsAppDeepLink: String? = null
)

enum class UploadTarget {
    PROFILE_PHOTO,
    ORDINATION_CERTIFICATE,
    IDENTITY_CERTIFICATE
}
