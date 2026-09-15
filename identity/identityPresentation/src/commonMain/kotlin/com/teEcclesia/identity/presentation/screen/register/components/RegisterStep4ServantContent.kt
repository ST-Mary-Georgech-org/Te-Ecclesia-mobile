package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.teEcclesia.identity.presentation.shared.components.EducationalStageFields
import com.teEcclesia.identity.presentation.screen.register.RegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.register.RegisterScreenState
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.servant_info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.identity.presentation.shared.components.ChildrenSelectionFields
import com.teEcclesia.identity.presentation.shared.components.PartnerSelectionFields
import teecclesia.designsystem.generated.resources.children
import teecclesia.designsystem.generated.resources.i_am_also_a_parent
import teecclesia.designsystem.generated.resources.partner

@Composable
fun RegisterStep4ServantContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.servant_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            EducationalStageFields(
                selectedStage = state.servantEducationalStage,
                onToggleStageSheet = listener::onToggleStageSheet,
                isStageSheetVisible = state.isStageSheetVisible,
                educationalStages = state.educationalStages,
                isStageLoading = state.isStageLoading,
                isStageLoadFailed = state.isStageLoadFailed,
                onRetryLoadStages = listener::onRetryLoadEducationalStages,
                onSelectEducationalStage = listener::onSelectEducationalStage,
                onLoadNextEducationalStages = listener::onLoadNextEducationalStages,
                stageError = state.stageError?.asString(),
                selectedYear = state.servantEducationalYear,
                onToggleYearSheet = listener::onToggleYearSheet,
                isYearSheetVisible = state.isYearSheetVisible,
                onSelectEducationalYear = listener::onSelectEducationalYear,
                yearError = state.yearError?.asString()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Checkbox(
                    checked = state.isAlsoParent,
                    onCheckedChange = listener::onToggleAlsoParent
                )
                Text(
                    text = stringResource(Res.string.i_am_also_a_parent),
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.onBackground,
                    modifier = Modifier.clickableNoRipple { listener.onToggleAlsoParent(!state.isAlsoParent) }
                )
            }

            AnimatedVisibility(
                visible = state.isAlsoParent,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.partner),
                        style = Theme.typography.headlineMedium,
                        color = Theme.colorScheme.onBackground
                    )

                    PartnerSelectionFields(
                        selectedPartner = state.selectedPartner,
                        partnerQuery = state.partnerQuery,
                        onPartnerQueryChange = listener::onPartnerQueryChange,
                        onSearchPartner = listener::onSearchPartner,
                        onRemovePartner = listener::onRemovePartner,
                        isLoading = state.isPartnerLoading,
                        errorText = state.partnerError?.asString()
                    )

                    Text(
                        text = stringResource(Res.string.children),
                        style = Theme.typography.headlineMedium,
                        color = Theme.colorScheme.onBackground
                    )

                    ChildrenSelectionFields(
                        childQuery = state.childQuery,
                        onChildQueryChange = listener::onChildQueryChange,
                        onSearchChild = listener::onSearchChild,
                        selectedChildren = state.selectedChildren,
                        onRemoveChild = listener::onRemoveChild,
                        isLoading = state.isChildLoading,
                        errorText = state.childError?.asString()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

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

@PreviewLightDark
@Composable
private fun RegisterStep4ServantContentPreviewLightDark() {
    var state by remember {
        mutableStateOf(
            RegisterScreenState(
                currentStep = 4,
                selectedRole = UserRole.KHADEM
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
            override fun onToggleOrdained(ordained: Boolean) {}
            override fun onSelectRank(rank: LookupResponse) {}
            override fun onToggleRankSheet(visible: Boolean) {}
            override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {}
            override fun onOrdinationYearChange(value: String) {}
            override fun onBishopNameChange(value: String) {}
            override fun onOrdinationPlaceChange(value: String) {}
            override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {}
            override fun onSelectEducationalStage(stage: LookupResponse) {}
            override fun onToggleStageSheet(visible: Boolean) {
                state = state.copy(isStageSheetVisible = visible)
            }

            override fun onSelectEducationalYear(year: LookupResponse) {}
            override fun onToggleYearSheet(visible: Boolean) {
                state = state.copy(isYearSheetVisible = visible)
            }

            override fun onToggleFatherDeceased(deceased: Boolean) {}
            override fun onFatherPhoneChange(value: String) {}
            override fun onFatherWhatsappChange(value: String) {}
            override fun onToggleMotherDeceased(deceased: Boolean) {}
            override fun onMotherPhoneChange(value: String) {}
            override fun onMotherWhatsappChange(value: String) {}
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
            override fun onRetryLoadPriests() {}
            override fun onRetryLoadAreas() {}
            override fun onLoadNextRanks() {}
            override fun onRetryLoadRanks() {}
            override fun onLoadNextEducationalStages() {}
            override fun onRetryLoadEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterStep4ServantContent(state = state, listener = listener)
        }
    }
}
