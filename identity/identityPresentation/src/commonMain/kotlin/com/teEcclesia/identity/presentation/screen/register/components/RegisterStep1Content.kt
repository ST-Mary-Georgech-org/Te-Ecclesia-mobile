package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.Button
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
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
import teecclesia.designsystem.generated.resources.personal_info
import teecclesia.designsystem.generated.resources.full_name_in_arabic
import teecclesia.designsystem.generated.resources.first_name
import teecclesia.designsystem.generated.resources.second_name
import teecclesia.designsystem.generated.resources.third_name
import teecclesia.designsystem.generated.resources.last_name
import teecclesia.designsystem.generated.resources.display_name
import teecclesia.designsystem.generated.resources.display_name_hint
import teecclesia.designsystem.generated.resources.national_id
import teecclesia.designsystem.generated.resources.national_id_hint
import teecclesia.designsystem.generated.resources.job
import teecclesia.designsystem.generated.resources.job_hint
import teecclesia.designsystem.generated.resources.confession_priest
import teecclesia.designsystem.generated.resources.from_another_church
import teecclesia.designsystem.generated.resources.confession_priest_name
import teecclesia.designsystem.generated.resources.confession_priest_church
import teecclesia.designsystem.generated.resources.confession_priest_phone
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.already_have_an_account
import teecclesia.designsystem.generated.resources.login
import teecclesia.designsystem.generated.resources.ic_chevron_down

@Composable
fun RegisterStep1Content(
    state: RegisterScreenState,
    listener: RegisterInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarPicker(
            imageBytes = state.imageBytes,
            imageUrl = state.imageUrl,
            onClick = { listener.onClickUpload(UploadTarget.PROFILE_PHOTO) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.personal_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            Text(
                text = stringResource(Res.string.full_name_in_arabic),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = listener::onFirstNameChange,
                    label = { Text(stringResource(Res.string.first_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    isError = state.firstNameError != null,
                    supportingText = state.firstNameError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.secondName,
                    onValueChange = listener::onSecondNameChange,
                    label = { Text(stringResource(Res.string.second_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    isError = state.secondNameError != null,
                    supportingText = state.secondNameError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.thirdName,
                    onValueChange = listener::onThirdNameChange,
                    label = { Text(stringResource(Res.string.third_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    isError = state.thirdNameError != null,
                    supportingText = state.thirdNameError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = listener::onLastNameChange,
                    label = { Text(stringResource(Res.string.last_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    isError = state.lastNameError != null,
                    supportingText = state.lastNameError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            OutlinedTextField(
                value = state.displayName,
                onValueChange = listener::onDisplayNameChange,
                label = { Text(stringResource(Res.string.display_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                supportingText = {
                    if (state.displayNameError != null) {
                        Text(state.displayNameError.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error)
                    } else {
                        Text(stringResource(Res.string.display_name_hint), style = Theme.typography.bodySmall, color = Theme.colorScheme.onSurfaceVariant)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.displayNameError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.nationalId,
                onValueChange = listener::onNationalIdChange,
                label = { Text(stringResource(Res.string.national_id), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                supportingText = {
                    if (state.nationalIdError != null) {
                        Text(state.nationalIdError.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error)
                    } else {
                        Text(stringResource(Res.string.national_id_hint), style = Theme.typography.bodySmall, color = Theme.colorScheme.onSurfaceVariant)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.nationalIdError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.job,
                onValueChange = listener::onJobChange,
                label = { Text(stringResource(Res.string.job), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                supportingText = {
                    Text(stringResource(Res.string.job_hint), style = Theme.typography.bodySmall, color = Theme.colorScheme.onSurfaceVariant)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                val priestText = when {
                    state.isFromAnotherChurch -> stringResource(Res.string.from_another_church)
                    state.selectedConfessionPriest != null -> state.selectedConfessionPriest.name
                    else -> ""
                }

                val interactionSource = remember { MutableInteractionSource() }

                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect { interaction ->
                        if (interaction is PressInteraction.Release) {
                            listener.onTogglePriestSheet(true)
                        }
                    }
                }

                OutlinedTextField(
                    value = priestText,
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.confession_priest), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth().clickableNoRipple { listener.onTogglePriestSheet(true) },
                    readOnly = true,
                    enabled = true,
                    isError = state.confessionPriestError != null,
                    supportingText = state.confessionPriestError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickableNoRipple { listener.onTogglePriestSheet(true) }
                        )
                    },
                    interactionSource = interactionSource
                )

                DropdownMenu(
                    expanded = state.isPriestSheetVisible,
                    onDismissRequest = { listener.onTogglePriestSheet(false) },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    state.confessionPriests.forEach { priest ->
                        DropdownMenuItem(
                            text = { Text(priest.name, style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurface) },
                            onClick = {
                                listener.onSelectConfessionPriest(priest)
                                listener.onTogglePriestSheet(false)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.from_another_church), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurface) },
                        onClick = {
                            listener.onSelectFromAnotherChurch()
                            listener.onTogglePriestSheet(false)
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isFromAnotherChurch,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.externalPriestName,
                        onValueChange = listener::onExternalPriestNameChange,
                        label = { Text(stringResource(Res.string.confession_priest_name), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.externalPriestNameError != null,
                        supportingText = state.externalPriestNameError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    OutlinedTextField(
                        value = state.externalPriestChurch,
                        onValueChange = listener::onExternalPriestChurchChange,
                        label = { Text(stringResource(Res.string.confession_priest_church), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.externalPriestChurchError != null,
                        supportingText = state.externalPriestChurchError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

                    OutlinedTextField(
                        value = state.externalPriestPhone,
                        onValueChange = listener::onExternalPriestPhoneChange,
                        label = { Text(stringResource(Res.string.confession_priest_phone), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.externalPriestPhoneError != null,
                        supportingText = state.externalPriestPhoneError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                        singleLine = true,
                        textStyle = Theme.typography.bodyLarge.copy(
                            textDirection = TextDirection.Ltr
                        ),
                        prefix = if (!isRtl) {
                            { Text("+2", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                        } else null,
                        suffix = if (isRtl) {
                            { Text("2+", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = listener::onClickNextStep,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = Theme.colorScheme.primary,
            contentColor = Theme.colorScheme.onPrimary,
            enabled = state.actionButtonState == AppButtonState.Enabled
        ) {
            Text(stringResource(Res.string.next), style = Theme.typography.labelLarge, color = Theme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(Res.string.already_have_an_account),
                color = Theme.colorScheme.onSurfaceVariant,
                style = Theme.typography.labelMedium
            )
            Text(
                text = stringResource(Res.string.login),
                color = Theme.colorScheme.primary,
                style = Theme.typography.labelLarge,
                modifier = Modifier.clickableNoRipple(onClick = listener::onClickLogin)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterStep1ContentPreviewLightDark() {
    var state by remember { mutableStateOf(RegisterScreenState()) }
    val listener = remember(state) {
        object : RegisterInteractionListener {
            override fun onClickNextStep() {}
            override fun onClickPreviousStep() {}
            override fun onClickLogin() {}
            override fun onFirstNameChange(value: String) { state = state.copy(firstName = value) }
            override fun onSecondNameChange(value: String) { state = state.copy(secondName = value) }
            override fun onThirdNameChange(value: String) { state = state.copy(thirdName = value) }
            override fun onLastNameChange(value: String) { state = state.copy(lastName = value) }
            override fun onDisplayNameChange(value: String) { state = state.copy(displayName = value) }
            override fun onNationalIdChange(value: String) { state = state.copy(nationalId = value) }
            override fun onJobChange(value: String) { state = state.copy(job = value) }
            override fun onSelectConfessionPriest(priest: Priest?) { state = state.copy(selectedConfessionPriest = priest) }
            override fun onSelectFromAnotherChurch() { state = state.copy(isFromAnotherChurch = true) }
            override fun onExternalPriestNameChange(value: String) { state = state.copy(externalPriestName = value) }
            override fun onExternalPriestChurchChange(value: String) { state = state.copy(externalPriestChurch = value) }
            override fun onExternalPriestPhoneChange(value: String) { state = state.copy(externalPriestPhone = value) }
            override fun onTogglePriestSheet(visible: Boolean) { state = state.copy(isPriestSheetVisible = visible) }
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
            override fun onToggleStageSheet(visible: Boolean) {}
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
            RegisterStep1Content(state = state, listener = listener)
        }
    }
}
