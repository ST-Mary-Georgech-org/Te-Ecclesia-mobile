package com.teEcclesia.identity.presentation.screen.register

import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.lookups.domain.model.LookupResponse

interface RegisterInteractionListener {
    // Navigation
    fun onClickNextStep()
    fun onClickPreviousStep()
    fun onClickLogin()

    // Step 1
    fun onFirstNameChange(value: String)
    fun onSecondNameChange(value: String)
    fun onThirdNameChange(value: String)
    fun onLastNameChange(value: String)
    fun onDisplayNameChange(value: String)
    fun onNationalIdChange(value: String)
    fun onJobChange(value: String)
    fun onSelectConfessionPriest(priest: Priest?)
    fun onSelectFromAnotherChurch()
    fun onExternalPriestNameChange(value: String)
    fun onExternalPriestChurchChange(value: String)
    fun onExternalPriestPhoneChange(value: String)
    fun onTogglePriestSheet(visible: Boolean)

    // Step 2
    fun onPhoneChange(value: String)
    fun onHomePhoneChange(value: String)
    fun onEmailChange(value: String)
    fun onPasswordChange(value: String)
    fun onTogglePasswordVisibility()
    fun onBuildingNoChange(value: String)
    fun onStreetChange(value: String)
    fun onStreetBranchChange(value: String)
    fun onAreaChange(value: String)
    fun onSelectArea(area: String)
    fun onToggleAreaSheet(visible: Boolean)
    fun onFloorChange(value: String)
    fun onApartmentChange(value: String)
    fun onSpecialMarkChange(value: String)

    // Step 3
    fun onRoleSelected(role: UserRole)

    // Step 4
    fun onToggleOrdained(ordained: Boolean)
    fun onSelectRank(rank: LookupResponse)
    fun onToggleRankSheet(visible: Boolean)
    fun onToggleOrdainedInThisChurch(inThisChurch: Boolean)
    fun onOrdinationYearChange(value: String)
    fun onBishopNameChange(value: String)
    fun onOrdinationPlaceChange(value: String)
    fun onShamamsaStatusSelected(status: ShamamsaStudyStatus)
    fun onSelectEducationalStage(stage: LookupResponse)
    fun onToggleEducationalStageSelection(stage: LookupResponse) {}
    fun onToggleStagesSheet(visible: Boolean) {}
    fun onToggleStageSheet(visible: Boolean)
    fun onSelectEducationalYear(year: LookupResponse)
    fun onToggleYearSheet(visible: Boolean)
    fun onToggleFatherDeceased(deceased: Boolean)
    fun onFatherPhoneChange(value: String)
    fun onFatherWhatsappChange(value: String)
    fun onToggleMotherDeceased(deceased: Boolean)
    fun onMotherPhoneChange(value: String)
    fun onMotherWhatsappChange(value: String)
    
    // Parent search
    fun onPartnerQueryChange(query: String)
    fun onSearchPartner()
    fun onRemovePartner()
    fun onChildQueryChange(query: String)
    fun onSearchChild()
    fun onRemoveChild(child: UserSummary)

    // Upload & Bottom Sheet
    fun onClickUpload(target: UploadTarget)
    fun onDismissUploadBottomSheet()
    fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?)
    
    // Step 5: Verify
    fun onClickVerifyWhatsApp()
    fun onClickCheckWhatsAppStatus()

    // Pagination
    fun onLoadNextPriests()
    fun onLoadNextRanks()
    fun onLoadNextEducationalStages()
}
