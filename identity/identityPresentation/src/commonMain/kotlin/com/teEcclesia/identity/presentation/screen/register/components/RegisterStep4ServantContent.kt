package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.Button
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.OutlinedTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.TeEcclesiaPreview
import com.teEcclesia.designsystem.utils.asString
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
import teecclesia.designsystem.generated.resources.servant_info
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.educational_year
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.ic_chevron_down

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

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.servantEducationalStage?.name ?: "",
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.educational_stage), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth().clickableNoRipple { listener.onToggleStageSheet(true) },
                    readOnly = true,
                    enabled = false,
                    isError = state.stageError != null,
                    supportingText = state.stageError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickableNoRipple { listener.onToggleStageSheet(true) }
                        )
                    }
                )

                BottomSheet(
                    isVisible = state.isStageSheetVisible,
                    onDismiss = { listener.onToggleStageSheet(false) }
                ) {
                    val stageListState = rememberLazyListState()

                    Text(
                        text = stringResource(Res.string.educational_stage),
                        style = Theme.typography.headlineSmall,
                        color = Theme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableNoRipple {
                                        listener.onSelectEducationalStage(stage)
                                        listener.onToggleStageSheet(false)
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                }
            }

            val hasYears = !state.servantEducationalStage?.subItems.isNullOrEmpty() || state.servantEducationalYear != null

            AnimatedVisibility(
                visible = hasYears,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.servantEducationalYear?.name ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(Res.string.educational_year), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurface) },
                        modifier = Modifier.fillMaxWidth().clickableNoRipple { listener.onToggleYearSheet(true) },
                        readOnly = true,
                        enabled = false,
                        isError = state.yearError != null,
                        supportingText = state.yearError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_chevron_down),
                                contentDescription = null,
                                tint = Theme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.clickableNoRipple { listener.onToggleYearSheet(true) }
                            )
                        }
                    )

                    BottomSheet(
                        isVisible = state.isYearSheetVisible,
                        onDismiss = { listener.onToggleYearSheet(false) }
                    ) {
                        Text(
                            text = stringResource(Res.string.educational_year),
                            style = Theme.typography.headlineSmall,
                            color = Theme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(
                                items = state.servantEducationalStage?.subItems ?: emptyList(),
                                key = { year -> year.id }
                            ) { year ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickableNoRipple {
                                            listener.onSelectEducationalYear(year)
                                            listener.onToggleYearSheet(false)
                                        }
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = year.name,
                                        style = Theme.typography.bodyLarge,
                                        color = Theme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = listener::onClickPreviousStep,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Theme.colorScheme.secondaryContainer,
                contentColor = Theme.colorScheme.onSecondaryContainer
            ) {
                Text(stringResource(Res.string.cancel), style = Theme.typography.labelLarge, color = Theme.colorScheme.onSecondaryContainer)
            }

            Button(
                onClick = listener::onClickNextStep,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Theme.colorScheme.primary,
                contentColor = Theme.colorScheme.onPrimary,
                enabled = state.actionButtonState == AppButtonState.Enabled
            ) {
                Text(stringResource(Res.string.next), style = Theme.typography.labelLarge, color = Theme.colorScheme.onPrimary)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterStep4ServantContentPreviewLightDark() {
    var state by remember { mutableStateOf(RegisterScreenState(currentStep = 4, selectedRole = UserRole.KHADEM)) }
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
            override fun onSelectEducationalStage(stage: LookupResponse) {  }
            override fun onToggleStageSheet(visible: Boolean) { state = state.copy(isStageSheetVisible = visible) }
            override fun onSelectEducationalYear(year: LookupResponse) {  }
            override fun onToggleYearSheet(visible: Boolean) { state = state.copy(isYearSheetVisible = visible) }
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
            override fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?) {}
            override fun onClickVerifyWhatsApp() {}
            override fun onClickCheckWhatsAppStatus() {}
            override fun onLoadNextPriests() {}
            override fun onLoadNextAreas() {}
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        TeEcclesiaPreview(darkTheme = Theme.isDarkTheme) {
            RegisterStep4ServantContent(state = state, listener = listener)
        }
    }
}
