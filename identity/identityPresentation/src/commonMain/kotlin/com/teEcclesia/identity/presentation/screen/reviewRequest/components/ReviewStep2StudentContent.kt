package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.radioButton.RadioButton
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.DeaconsSchoolStatus
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.screen.reviewRequest.toUiText
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.presentation.shared.components.DeaconSchoolFields
import com.teEcclesia.identity.presentation.shared.components.EducationalStageFields
import com.teEcclesia.identity.presentation.shared.components.OrdinationInfoFields
import com.teEcclesia.identity.presentation.shared.components.ParentsContactFields
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.amount_paid
import teecclesia.designsystem.generated.resources.deacons_school_details
import teecclesia.designsystem.generated.resources.deacons_school_status
import teecclesia.designsystem.generated.resources.enrolled_in_deacons_school
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.ic_ordination
import teecclesia.designsystem.generated.resources.ic_school
import teecclesia.designsystem.generated.resources.file_ordination_certificate
import teecclesia.designsystem.generated.resources.file_identity_card
import teecclesia.designsystem.generated.resources.identity_card_certificate_optional
import teecclesia.designsystem.generated.resources.ordination_certificate_optional
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.paid
import teecclesia.designsystem.generated.resources.payment_status
import teecclesia.designsystem.generated.resources.personal_info
import teecclesia.designsystem.generated.resources.unpaid

@Composable
fun ReviewStep2StudentContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
    onFileClickOrdinationCertificate: (() -> Unit)? = null,
    onFileClickIdentityCertificate: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.deacons_school_details),
            icon = painterResource(Res.drawable.ic_school)
        ) {
            DeaconSchoolFields(
                shamamsaStatus = state.shamamsaStatus,
                onShamamsaStatusSelected = listener::onShamamsaStatusSelected
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.isEnrolledInDeaconSchool,
                    onCheckedChange = { listener.onToggleEnrolledInDeaconSchool(it) },
                    enabled = state.isAdmin
                )
                Text(
                    text = stringResource(Res.string.enrolled_in_deacons_school),
                    style = Theme.typography.bodyMedium,
                    color = if (state.isAdmin) Theme.colorScheme.onBackground else Theme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = state.isEnrolledInDeaconSchool,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(Res.string.payment_status),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onBackground
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = state.isDeaconSchoolPaid,
                                    onClick = { if (state.isAdmin) listener.onToggleDeaconSchoolPaid(true) },
                                    enabled = state.isAdmin
                                )
                                Text(
                                    text = stringResource(Res.string.paid),
                                    style = Theme.typography.bodyMedium,
                                    color = Theme.colorScheme.onBackground
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = !state.isDeaconSchoolPaid,
                                    onClick = { if (state.isAdmin) listener.onToggleDeaconSchoolPaid(false) },
                                    enabled = state.isAdmin
                                )
                                Text(
                                    text = stringResource(Res.string.unpaid),
                                    style = Theme.typography.bodyMedium,
                                    color = Theme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    CustomTextField(
                        value = state.deaconSchoolPaidAmount,
                        onValueChange = listener::onDeaconSchoolPaidAmountChange,
                        labelText = stringResource(Res.string.amount_paid),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.isAdmin,
                        readOnly = !state.isAdmin,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomTextField(
                            value = state.deaconSchoolStatus.toUiText().asString(),
                            onValueChange = {},
                            labelText = stringResource(Res.string.deacons_school_status),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { if (state.isAdmin) listener.onToggleDeaconSchoolStatusSheet(true) },
                            readOnly = true,
                            enabled = false,
                            trailingIcon = if (state.isAdmin) painterResource(Res.drawable.ic_chevron_down) else null
                        )

                        DropdownMenu(
                            expanded = state.isDeaconSchoolStatusSheetVisible,
                            onDismissRequest = { listener.onToggleDeaconSchoolStatusSheet(false) },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            DeaconsSchoolStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = status.toUiText().asString(),
                                            style = Theme.typography.bodyMedium,
                                            color = Theme.colorScheme.onBackground
                                        )
                                    },
                                    onClick = {
                                        listener.onDeaconSchoolStatusSelected(status)
                                        listener.onToggleDeaconSchoolStatusSheet(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.isMale != false) {
            ReviewSectionCard(
                title = stringResource(Res.string.ordination_info),
                icon = painterResource(Res.drawable.ic_ordination)
            ) {
                OrdinationInfoFields(
                    isOrdained = state.isOrdained,
                    onToggleOrdained = listener::onToggleOrdained,
                    selectedRank = state.selectedRank,
                    ranks = state.ranks,
                    isRankSheetVisible = state.isRankSheetVisible,
                    onToggleRankSheet = listener::onToggleRankSheet,
                    onSelectRank = listener::onSelectRank,
                    rankError = state.rankError?.asString(),
                    isRankLoading = state.isRankLoading,
                    isRankLoadFailed = state.isRankLoadFailed,
                    onRetryLoadRanks = listener::onRetryLoadRanks,
                    isOrdainedInThisChurch = state.isOrdainedInThisChurch,
                    onToggleOrdainedInThisChurch = listener::onToggleOrdainedInThisChurch,
                    ordinationYear = state.ordinationYear,
                    onOrdinationYearChange = listener::onOrdinationYearChange,
                    ordinationYearError = state.ordinationYearError?.asString(),
                    bishopName = state.bishopName,
                    onBishopNameChange = listener::onBishopNameChange,
                    ordinationPlace = state.ordinationPlace,
                    onOrdinationPlaceChange = listener::onOrdinationPlaceChange,
                    filePickerContent = {
                        ReviewFilePickerRow(
                            label = stringResource(Res.string.ordination_certificate_optional),
                            fileTitle = stringResource(Res.string.file_ordination_certificate),
                            fileName = state.ordinationCertificateFileName,
                            onUploadClick = { listener.onClickUpload(UploadTarget.ORDINATION_CERTIFICATE) },
                            onClearClick = {
                                listener.onSelectImageBytes(UploadTarget.ORDINATION_CERTIFICATE, null, null)
                            },
                            onFileClick = onFileClickOrdinationCertificate
                        )
                    }
                )
            }
        }

        ReviewSectionCard(
            title = stringResource(Res.string.personal_info),
            icon = painterResource(Res.drawable.ic_family)
        ) {
            EducationalStageFields(
                selectedStage = state.studentEducationalStage,
                onToggleStageSheet = listener::onToggleStageSheet,
                isStageSheetVisible = state.isStageSheetVisible,
                educationalStages = state.educationalStages,
                isStageLoading = state.isStageLoading,
                isStageLoadFailed = state.isStageLoadFailed,
                onRetryLoadStages = listener::onRetryLoadEducationalStages,
                onSelectEducationalStage = listener::onSelectEducationalStage,
                onLoadNextEducationalStages = listener::onLoadNextEducationalStages,
                stageError = state.stageError?.asString(),
                selectedYear = state.studentEducationalYear,
                onToggleYearSheet = listener::onToggleYearSheet,
                isYearSheetVisible = state.isYearSheetVisible,
                onSelectEducationalYear = listener::onSelectEducationalYear,
                yearError = state.yearError?.asString()
            )

            ParentsContactFields(
                isFatherDeceased = state.isFatherDeceased,
                onToggleFatherDeceased = listener::onToggleFatherDeceased,
                fatherPhone = state.fatherPhone,
                onFatherPhoneChange = listener::onFatherPhoneChange,
                fatherPhoneError = state.fatherPhoneError?.asString(),
                fatherWhatsapp = state.fatherWhatsapp,
                onFatherWhatsappChange = listener::onFatherWhatsappChange,
                fatherWhatsappError = state.fatherWhatsappError?.asString(),
                isFatherWhatsappSameAsPhone = state.isFatherWhatsappSameAsPhone,
                onToggleFatherWhatsappSameAsPhone = listener::onToggleFatherWhatsappSameAsPhone,
                isMotherDeceased = state.isMotherDeceased,
                onToggleMotherDeceased = listener::onToggleMotherDeceased,
                motherPhone = state.motherPhone,
                onMotherPhoneChange = listener::onMotherPhoneChange,
                motherPhoneError = state.motherPhoneError?.asString(),
                motherWhatsapp = state.motherWhatsapp,
                onMotherWhatsappChange = listener::onMotherWhatsappChange,
                motherWhatsappError = state.motherWhatsappError?.asString(),
                isMotherWhatsappSameAsPhone = state.isMotherWhatsappSameAsPhone,
                onToggleMotherWhatsappSameAsPhone = listener::onToggleMotherWhatsappSameAsPhone,
                filePickerContent = {
                    ReviewFilePickerRow(
                        label = stringResource(Res.string.identity_card_certificate_optional),
                        fileTitle = stringResource(Res.string.file_identity_card),
                        fileName = state.identityCertificateFileName,
                        onUploadClick = { listener.onClickUpload(UploadTarget.IDENTITY_CERTIFICATE) },
                        onClearClick = {
                            listener.onSelectImageBytes(UploadTarget.IDENTITY_CERTIFICATE, null, null)
                        },
                        onFileClick = onFileClickIdentityCertificate
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun ReviewStep2StudentContentPreview() = Theme {
    val state = ReviewAndEditRequestUiState(
        userId = "123",
        isLoading = false,
        isAdmin = true,
        isEnrolledInDeaconSchool = true,
        deaconSchoolPaidAmount = "500"
    )
    val listener = object : ReviewAndEditRequestInteractionListener {
        override fun onClickBack() {}
        override fun onNextStep() {}
        override fun onPreviousStep() {}
        override fun onStepClicked(step: Int) {}
        override fun onApproveRequest() {}
        override fun onRejectRequest(reason: String) {}
        override fun onToggleRejectDialog(isVisible: Boolean) {}
        override fun onRefresh() {}
        override fun onClickUpload(target: UploadTarget) {}
        override fun onDismissUploadBottomSheet() {}
        override fun onDismissImageViewer() {}
        override fun onDismissPdfViewer() {}
        override fun onClickOrdinationCertificate() {}
        override fun onClickIdentityCertificate() {}
        override fun onCodeChanged(value: String) {}
        override fun onFirstNameChanged(value: String) {}
        override fun onSecondNameChanged(value: String) {}
        override fun onThirdNameChanged(value: String) {}
        override fun onLastNameChanged(value: String) {}
        override fun onDisplayNameChanged(value: String) {}
        override fun onNationalIdChanged(value: String) {}
        override fun onJobChanged(value: String) {}
        override fun onIsFromAnotherChurchChanged(value: Boolean) {}
        override fun onConfessionPriestIdChanged(value: String?) {}
        override fun onConfessionPriestNameChanged(value: String) {}
        override fun onConfessionPriestChurchChanged(value: String) {}
        override fun onConfessionPriestPhoneChanged(value: String) {}
        override fun onPhoneChanged(value: String) {}
        override fun onHomePhoneChanged(value: String) {}
        override fun onEmailChanged(email: String) {}
        override fun onPasswordChanged(password: String) {}
        override fun onTogglePasswordVisibility() {}
        override fun onBuildingNoChanged(value: String) {}
        override fun onStreetChanged(value: String) {}
        override fun onStreetBranchChanged(value: String) {}
        override fun onAreaChanged(value: String) {}
        override fun onFloorChanged(value: String) {}
        override fun onApartmentChanged(value: String) {}
        override fun onSpecialMarkChanged(value: String) {}
        override fun onSelectArea(area: String) {}
        override fun onToggleAreaSheet(visible: Boolean) {}
        override fun onSelectConfessionPriest(priest: Priest?) {}
        override fun onSelectFromAnotherChurch() {}
        override fun onTogglePriestSheet(visible: Boolean) {}
        override fun onLoadNextPriests() {}
        override fun onFileOptionPicked(option: FilePickOption) {}
        override fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?) {}
        override fun onRoleSelected(role: UserRole) {}
        override fun onToggleRoleSheet(visible: Boolean) {}
        override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {}
        override fun onToggleOrdained(ordained: Boolean) {}
        override fun onSelectRank(rank: LookupResponse) {}
        override fun onToggleRankSheet(visible: Boolean) {}
        override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {}
        override fun onOrdinationYearChange(value: String) {}
        override fun onBishopNameChange(value: String) {}
        override fun onOrdinationPlaceChange(value: String) {}
        override fun onSelectEducationalStage(stage: LookupResponse) {}
        override fun onToggleStageSheet(visible: Boolean) {}
        override fun onSelectEducationalYear(year: LookupResponse) {}
        override fun onToggleYearSheet(visible: Boolean) {}
        override fun onToggleFatherDeceased(deceased: Boolean) {}
        override fun onFatherPhoneChange(value: String) {}
        override fun onFatherWhatsappChange(value: String) {}
        override fun onToggleFatherWhatsappSameAsPhone(isSame: Boolean) {}
        override fun onToggleMotherDeceased(deceased: Boolean) {}
        override fun onMotherPhoneChange(value: String) {}
        override fun onMotherWhatsappChange(value: String) {}
        override fun onToggleMotherWhatsappSameAsPhone(isSame: Boolean) {}
        override fun onToggleServantStageSelection(stage: LookupResponse) {}
        override fun onToggleServantStageSheet(visible: Boolean) {}
        override fun onToggleServantYearSelection(year: LookupResponse) {}
        override fun onToggleServantYearSheet(visible: Boolean) {}
        override fun onToggleCanApproveNewRequests(canApprove: Boolean) {}
        override fun onToggleResponsibleStageSelection(stage: LookupResponse) {}
        override fun onToggleResponsibleStageSheet(visible: Boolean) {}
        override fun onToggleResponsibleYearSelection(year: LookupResponse) {}
        override fun onToggleResponsibleYearSheet(visible: Boolean) {}
        override fun onPartnerQueryChange(query: String) {}
        override fun onSearchPartner() {}
        override fun onRemovePartner() {}
        override fun onChildQueryChange(query: String) {}
        override fun onSearchChild() {}
        override fun onRemoveChild(child: UserSummary) {}
        override fun onToggleEducationalStageSelection(stage: LookupResponse) {}
        override fun onToggleStagesSheet(visible: Boolean) {}
        override fun onLoadNextEducationalStages() {}
        override fun onRetryLoadEducationalStages() {}
        override fun onLoadNextRanks() {}
        override fun onRetryLoadRanks() {}
        override fun onRetryLoadPriests() {}
        override fun onRetryLoadAreas() {}
        override fun onNotesChanged(value: String) {}
        override fun onToggleEnrolledInDeaconSchool(enrolled: Boolean) {}
        override fun onToggleDeaconSchoolPaid(isPaid: Boolean) {}
        override fun onDeaconSchoolPaidAmountChange(amount: String) {}
        override fun onDeaconSchoolStatusSelected(status: DeaconsSchoolStatus) {}
        override fun onToggleDeaconSchoolStatusSheet(visible: Boolean) {}
    }
    Preview {
        ReviewStep2StudentContent(
            state = state,
            listener = listener
        )
    }
}
