package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.RegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.register.RegisterScreenState
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.educational_stages
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.ok
import teecclesia.designsystem.generated.resources.priest_info

@Composable
fun RegisterStep4KahenContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.priest_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                val selectedText = state.kahenEducationalStages.joinToString(", ") { it.name }
                val currentError = state.stageError ?: state.stagesError
                CustomTextField(
                    value = selectedText,
                    onValueChange = {},
                    labelText = stringResource(Res.string.educational_stages),
                    onClick = {
                        focusManager.clearFocus()
                        listener.onToggleStagesSheet(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    readOnly = true,
                    enabled = false,
                    errorText = currentError?.asString(),
                    trailingIcon = painterResource(Res.drawable.ic_chevron_down)
                )
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

    BottomSheet(
        isVisible = state.isStagesSheetVisible,
        skipPartiallyExpanded = true,
        onDismiss = { listener.onToggleStagesSheet(false) }
    ) {
        val stageListState = rememberLazyListState()

        Text(
            text = stringResource(Res.string.educational_stages),
            style = Theme.typography.headlineSmall,
            color = Theme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )


        LazyColumn(
            state = stageListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = state.educationalStages,
                key = { stage -> stage.id }
            ) { stage ->
                val isSelected = state.kahenEducationalStages.any { it.id == stage.id }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple {
                            listener.onToggleEducationalStageSelection(stage)
                        }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            listener.onToggleEducationalStageSelection(stage)
                        },
                        checkedColor = Theme.colorScheme.primary,
                        uncheckedColor = Theme.colorScheme.outline
                    )
                    Text(
                        text = stage.name,
                        style = Theme.typography.bodyLarge,
                        color = Theme.colorScheme.onSurface
                    )
                }
            }
        }

        PaginationTrigger(
            list = state.educationalStages,
            listState = stageListState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadNextEducationalStages
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            type = AppButtonType.Primary,
            onClick = { listener.onToggleStagesSheet(false) },
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.ok)
        )
    }
}

@PreviewLightDark
@Composable
private fun RegisterStep4KahenContentPreviewLightDark() {
    var state by remember {
        mutableStateOf(
            RegisterScreenState(
                currentStep = 4,
                selectedRole = UserRole.KAHEN
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
            override fun onToggleEducationalStageSelection(stage: LookupResponse) {

            }

            override fun onToggleStagesSheet(visible: Boolean) {
                state = state.copy(isStagesSheetVisible = visible)
            }

            override fun onToggleStageSheet(visible: Boolean) {
                state = state.copy(isStageSheetVisible = visible)
            }

            override fun onSelectEducationalYear(year: LookupResponse) {}
            override fun onToggleYearSheet(visible: Boolean) {}
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
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterStep4KahenContent(state = state, listener = listener)
        }
    }
}

