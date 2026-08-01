package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.RegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.register.RegisterScreenState
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.shared.components.DeaconSchoolFields
import com.teEcclesia.identity.presentation.shared.components.EducationalStageFields
import com.teEcclesia.identity.presentation.shared.components.OrdinationInfoFields
import com.teEcclesia.identity.presentation.shared.components.ParentsContactFields
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.deacons_school_details
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.personal_info

@Composable
fun RegisterStep4StudentContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener,
    onFileClickOrdinationCertificate: (() -> Unit)? = null,
    onFileClickIdentityCertificate: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.deacons_school_details),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            DeaconSchoolFields(
                shamamsaStatus = state.shamamsaStatus,
                onShamamsaStatusSelected = listener::onShamamsaStatusSelected
            )


            if (state.isMale != false) {
                Text(
                    text = stringResource(Res.string.ordination_info),
                    style = Theme.typography.headlineMedium,
                    color = Theme.colorScheme.onBackground
                )

                OrdinationInfoFields(
                    isOrdained = state.isOrdained,
                    onToggleOrdained = listener::onToggleOrdained,
                    selectedRank = state.selectedRank,
                    ranks = state.ranks,
                    isRankSheetVisible = state.isRankSheetVisible,
                    onToggleRankSheet = listener::onToggleRankSheet,
                    onSelectRank = listener::onSelectRank,
                    rankError = state.rankError?.asString(),
                    isOrdainedInThisChurch = state.isOrdainedInThisChurch,
                    onToggleOrdainedInThisChurch = listener::onToggleOrdainedInThisChurch,
                    ordinationYear = state.ordinationYear,
                    onOrdinationYearChange = listener::onOrdinationYearChange,
                    ordinationYearError = state.ordinationYearError?.asString(),
                    bishopName = state.bishopName,
                    onBishopNameChange = listener::onBishopNameChange,
                    ordinationPlace = state.ordinationPlace,
                    onOrdinationPlaceChange = listener::onOrdinationPlaceChange,
                    ordinationCertificateFileName = state.ordinationCertificateFileName,
                    onUploadOrdinationCertificate = { listener.onClickUpload(UploadTarget.ORDINATION_CERTIFICATE) },
                    onClearOrdinationCertificate = {
                        listener.onSelectImageBytes(UploadTarget.ORDINATION_CERTIFICATE, null, null)
                    },
                    onFileClickOrdinationCertificate = onFileClickOrdinationCertificate
                )

                Spacer(modifier = Modifier.height(8.dp))
            }


            Text(
                text = stringResource(Res.string.personal_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            EducationalStageFields(
                selectedStage = state.studentEducationalStage,
                onToggleStageSheet = listener::onToggleStageSheet,
                isStageSheetVisible = state.isStageSheetVisible,
                educationalStages = state.educationalStages,
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
                isMotherDeceased = state.isMotherDeceased,
                onToggleMotherDeceased = listener::onToggleMotherDeceased,
                motherPhone = state.motherPhone,
                onMotherPhoneChange = listener::onMotherPhoneChange,
                motherPhoneError = state.motherPhoneError?.asString(),
                motherWhatsapp = state.motherWhatsapp,
                onMotherWhatsappChange = listener::onMotherWhatsappChange,
                motherWhatsappError = state.motherWhatsappError?.asString(),
                identityCertificateFileName = state.identityCertificateFileName,
                onUploadIdentityCertificate = { listener.onClickUpload(UploadTarget.IDENTITY_CERTIFICATE) },
                onClearIdentityCertificate = {
                    listener.onSelectImageBytes(UploadTarget.IDENTITY_CERTIFICATE, null, null)
                },
                onFileClickIdentityCertificate = onFileClickIdentityCertificate
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                type = AppButtonType.Secondary,
                onClick = listener::onClickPreviousStep,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel)
            )

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onClickNextStep,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.next),
                state = state.actionButtonState
            )
        }
    }
}

@Preview(heightDp = 1600)
@Composable
private fun RegisterStep4StudentContentPreviewLightDark() {
    var state by remember {
        mutableStateOf(
            RegisterScreenState(
                currentStep = 4,
                selectedRole = UserRole.MAKHDOOM,
                isMale = true
            )
        )
    }
    val listener = remember(state) {
        object : RegisterInteractionListener {
            override fun onClickNextStep() {}
            override fun onClickPreviousStep() {}
            override fun onClickLogin() {}
            override fun onFirstNameChange(value: String) {}
            override fun onSecondNameChange(value: String) {}
            override fun onThirdNameChange(value: String) {}
            override fun onLastNameChange(value: String) {}
            override fun onDisplayNameChange(value: String) {}
            override fun onNationalIdChange(value: String) {}
            override fun onJobChange(value: String) {}
            override fun onSelectConfessionPriest(priest: Priest?) {}
            override fun onSelectFromAnotherChurch() {}
            override fun onExternalPriestNameChange(value: String) {}
            override fun onExternalPriestChurchChange(value: String) {}
            override fun onExternalPriestPhoneChange(value: String) {}
            override fun onTogglePriestSheet(visible: Boolean) {}
            override fun onPhoneChange(value: String) {}
            override fun onHomePhoneChange(value: String) {}
            override fun onEmailChange(value: String) {}
            override fun onPasswordChange(value: String) {}
            override fun onTogglePasswordVisibility() {}
            override fun onBuildingNoChange(value: String) {}
            override fun onStreetChange(value: String) {}
            override fun onStreetBranchChange(value: String) {}
            override fun onAreaChange(value: String) {}
            override fun onSelectArea(area: String) {}
            override fun onToggleAreaSheet(visible: Boolean) {}
            override fun onFloorChange(value: String) {}
            override fun onApartmentChange(value: String) {}
            override fun onSpecialMarkChange(value: String) {}
            override fun onRoleSelected(role: UserRole) {}
            override fun onToggleOrdained(ordained: Boolean) {
                state = state.copy(
                    isOrdained = ordained,
                    ordinationYearError = if (!ordained) null else state.ordinationYearError,
                    rankError = if (!ordained) null else state.rankError
                )
            }

            override fun onSelectRank(rank: LookupResponse) {
                state = state.copy(selectedRank = rank)
            }

            override fun onToggleRankSheet(visible: Boolean) {
                state = state.copy(isRankSheetVisible = visible)
            }

            override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {
                state = state.copy(isOrdainedInThisChurch = inThisChurch)
            }

            override fun onOrdinationYearChange(value: String) {
                state = state.copy(ordinationYear = value, ordinationYearError = null)
            }

            override fun onBishopNameChange(value: String) {
                state = state.copy(bishopName = value)
            }

            override fun onOrdinationPlaceChange(value: String) {
                state = state.copy(ordinationPlace = value)
            }

            override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {
                state = state.copy(shamamsaStatus = status)
            }

            override fun onSelectEducationalStage(stage: LookupResponse) {}
            override fun onToggleStageSheet(visible: Boolean) {
                state = state.copy(isStageSheetVisible = visible)
            }

            override fun onSelectEducationalYear(year: LookupResponse) {}
            override fun onToggleYearSheet(visible: Boolean) {
                state = state.copy(isYearSheetVisible = visible)
            }

            override fun onToggleFatherDeceased(deceased: Boolean) {
                state = state.copy(
                    isFatherDeceased = deceased,
                    fatherPhoneError = if (deceased) null else state.fatherPhoneError,
                    fatherWhatsappError = if (deceased) null else state.fatherWhatsappError
                )
            }

            override fun onFatherPhoneChange(value: String) {
                state = state.copy(fatherPhone = value, fatherPhoneError = null)
            }

            override fun onFatherWhatsappChange(value: String) {
                state = state.copy(fatherWhatsapp = value, fatherWhatsappError = null)
            }

            override fun onToggleMotherDeceased(deceased: Boolean) {
                state = state.copy(
                    isMotherDeceased = deceased,
                    motherPhoneError = if (deceased) null else state.motherPhoneError,
                    motherWhatsappError = if (deceased) null else state.motherWhatsappError
                )
            }

            override fun onMotherPhoneChange(value: String) {
                state = state.copy(motherPhone = value, motherPhoneError = null)
            }

            override fun onMotherWhatsappChange(value: String) {
                state = state.copy(motherWhatsapp = value, motherWhatsappError = null)
            }

            override fun onPartnerQueryChange(query: String) {}
            override fun onSearchPartner() {}
            override fun onRemovePartner() {}
            override fun onChildQueryChange(query: String) {}
            override fun onSearchChild() {}
            override fun onRemoveChild(child: UserSummary) {}
            override fun onClickUpload(target: UploadTarget) {}
            override fun onDismissUploadBottomSheet() {}
            override fun onSelectImageBytes(
                target: UploadTarget,
                bytes: ByteArray?,
                fileName: String?
            ) {
            }

            override fun onClickVerifyWhatsApp() {}
            override fun onClickCheckWhatsAppStatus() {}
            override fun onLoadNextPriests() {}
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterStep4StudentContent(state = state, listener = listener)
        }
    }
}
