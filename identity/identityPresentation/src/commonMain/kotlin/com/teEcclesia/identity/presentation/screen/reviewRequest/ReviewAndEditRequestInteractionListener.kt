package com.teEcclesia.identity.presentation.screen.reviewRequest

import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.lookups.domain.model.LookupResponse

interface ReviewAndEditRequestInteractionListener {
    fun onClickBack()
    fun onNextStep()
    fun onPreviousStep()
    fun onStepClicked(step: Int)
    fun onApproveRequest()
    fun onRejectRequest(reason: String)
    fun onToggleRejectDialog(isVisible: Boolean)
    fun onRefresh()

    fun onClickUpload(target: UploadTarget)
    fun onDismissUploadBottomSheet()
    fun onDismissImageViewer() {}
    fun onDismissPdfViewer() {}
    fun onClickOrdinationCertificate() {}
    fun onClickIdentityCertificate() {}
    fun onCodeChanged(value: String)
    fun onFirstNameChanged(value: String)
    fun onSecondNameChanged(value: String)
    fun onThirdNameChanged(value: String)
    fun onLastNameChanged(value: String)
    fun onDisplayNameChanged(value: String)
    fun onNationalIdChanged(value: String)
    fun onJobChanged(value: String)
    fun onIsFromAnotherChurchChanged(value: Boolean)
    fun onConfessionPriestIdChanged(value: String?)
    fun onConfessionPriestNameChanged(value: String)
    fun onConfessionPriestChurchChanged(value: String)
    fun onConfessionPriestPhoneChanged(value: String)
    fun onPhoneChanged(value: String)
    fun onHomePhoneChanged(value: String)
    fun onEmailChanged(email: String)
    fun onPasswordChanged(password: String)
    fun onTogglePasswordVisibility()
    fun onBuildingNoChanged(value: String)
    fun onStreetChanged(value: String)
    fun onStreetBranchChanged(value: String)
    fun onAreaChanged(value: String)
    fun onFloorChanged(value: String)
    fun onApartmentChanged(value: String)
    fun onSpecialMarkChanged(value: String)

    fun onSelectArea(area: String)
    fun onToggleAreaSheet(visible: Boolean)
    fun onSelectConfessionPriest(priest: Priest?)
    fun onSelectFromAnotherChurch()
    fun onTogglePriestSheet(visible: Boolean)
    fun onLoadNextPriests()
    fun onFileOptionPicked(option: FilePickOption)
    fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?)

    fun onRoleSelected(role: UserRole)
    fun onToggleRoleSheet(visible: Boolean)
    fun onShamamsaStatusSelected(status: ShamamsaStudyStatus)
    fun onToggleOrdained(ordained: Boolean)
    fun onSelectRank(rank: LookupResponse)
    fun onToggleRankSheet(visible: Boolean)
    fun onToggleOrdainedInThisChurch(inThisChurch: Boolean)
    fun onOrdinationYearChange(value: String)
    fun onBishopNameChange(value: String)
    fun onOrdinationPlaceChange(value: String)
    fun onSelectEducationalStage(stage: LookupResponse)
    fun onToggleStageSheet(visible: Boolean)
    fun onSelectEducationalYear(year: LookupResponse)
    fun onToggleYearSheet(visible: Boolean)
    fun onToggleFatherDeceased(deceased: Boolean)
    fun onFatherPhoneChange(value: String)
    fun onFatherWhatsappChange(value: String)
    fun onToggleFatherWhatsappSameAsPhone(isSame: Boolean) {}
    fun onToggleMotherDeceased(deceased: Boolean)
    fun onMotherPhoneChange(value: String)
    fun onMotherWhatsappChange(value: String)
    fun onToggleMotherWhatsappSameAsPhone(isSame: Boolean) {}

    fun onToggleServantStageSelection(stage: LookupResponse)
    fun onToggleServantStageSheet(visible: Boolean)
    fun onToggleServantYearSelection(year: LookupResponse)
    fun onToggleServantYearSheet(visible: Boolean)
    fun onToggleCanApproveNewRequests(canApprove: Boolean)
    fun onToggleResponsibleStageSelection(stage: LookupResponse)
    fun onToggleResponsibleStageSheet(visible: Boolean)
    fun onToggleResponsibleYearSelection(year: LookupResponse)
    fun onToggleResponsibleYearSheet(visible: Boolean)

    fun onPartnerQueryChange(query: String)
    fun onSearchPartner()
    fun onRemovePartner()
    fun onChildQueryChange(query: String)
    fun onSearchChild()
    fun onRemoveChild(child: UserSummary)

    fun onToggleEducationalStageSelection(stage: LookupResponse)
    fun onToggleStagesSheet(visible: Boolean)
    fun onLoadNextEducationalStages()
    fun onRetryLoadEducationalStages()
    fun onLoadNextRanks()
    fun onRetryLoadRanks()
    fun onRetryLoadPriests()
    fun onRetryLoadAreas()

    fun onNotesChanged(value: String)
}
