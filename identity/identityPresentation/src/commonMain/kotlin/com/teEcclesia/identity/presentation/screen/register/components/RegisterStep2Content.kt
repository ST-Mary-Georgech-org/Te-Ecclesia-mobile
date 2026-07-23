package com.teEcclesia.identity.presentation.screen.register.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.phone_number
import teecclesia.designsystem.generated.resources.home_phone
import teecclesia.designsystem.generated.resources.email_optional
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.address_info
import teecclesia.designsystem.generated.resources.building_no
import teecclesia.designsystem.generated.resources.street
import teecclesia.designsystem.generated.resources.branching_from
import teecclesia.designsystem.generated.resources.area
import teecclesia.designsystem.generated.resources.floor
import teecclesia.designsystem.generated.resources.apartment
import teecclesia.designsystem.generated.resources.special_mark
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.invalid_password_format
import teecclesia.designsystem.generated.resources.account_and_contact
import teecclesia.designsystem.generated.resources.phone_number_supporting_text
import teecclesia.designsystem.generated.resources.you_should_have_whatsapp_on_this_phone

@Composable
fun RegisterStep2Content(
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
                text = stringResource(Res.string.account_and_contact),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

            OutlinedTextField(
                value = state.phone,
                onValueChange = listener::onPhoneChange,
                label = { Text(stringResource(Res.string.phone_number), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.phoneError != null,
                supportingText = state.phoneError?.let {
                    {
                        Text(
                            it.asString(),
                            style = Theme.typography.bodySmall,
                            color = Theme.colorScheme.error
                        )
                    }
                } ?: {
                    Text(
                        stringResource(Res.string.you_should_have_whatsapp_on_this_phone),
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                },
                textStyle = Theme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Ltr
                ),
                prefix = if (!isRtl) {
                    { Text("+2", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                } else null,
                suffix = if (isRtl) {
                    { Text("2+", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.homePhone,
                onValueChange = listener::onHomePhoneChange,
                label = { Text(stringResource(Res.string.home_phone), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = state.homePhoneError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                textStyle = Theme.typography.bodyLarge.copy(
                    textDirection = TextDirection.Ltr
                ),
                prefix = if (!isRtl) {
                    { Text("02", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                } else null,
                suffix = if (isRtl) {
                    { Text("02", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurface) }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = listener::onEmailChange,
                label = { Text(stringResource(Res.string.email_optional), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.emailError != null,
                supportingText = state.emailError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = listener::onPasswordChange,
                label = { Text(stringResource(Res.string.password), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                supportingText = {
                    if (state.passwordError != null) {
                        Text(state.passwordError.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error)
                    } else {
                        Text(stringResource(Res.string.invalid_password_format), style = Theme.typography.bodySmall, color = Theme.colorScheme.onSurfaceVariant)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.passwordError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(if (state.isPasswordVisible) Res.drawable.ic_eye_closed else Res.drawable.ic_eye_opened),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickableNoRipple(onClick = listener::onTogglePasswordVisibility)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.address_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = state.buildingNo,
                onValueChange = listener::onBuildingNoChange,
                label = { Text(stringResource(Res.string.building_no), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.buildingNoError != null,
                supportingText = state.buildingNoError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = state.street,
                onValueChange = listener::onStreetChange,
                label = { Text(stringResource(Res.string.street), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.streetError != null,
                supportingText = state.streetError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                singleLine = true
            )

            OutlinedTextField(
                value = state.streetBranch,
                onValueChange = listener::onStreetBranchChange,
                label = { Text(stringResource(Res.string.branching_from), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.selectedArea ?: "",
                    onValueChange = listener::onAreaChange,
                    label = { Text(stringResource(Res.string.area), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.areaError != null,
                    supportingText = state.areaError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickableNoRipple { listener.onToggleAreaSheet(!state.isAreaSheetVisible) }
                        )
                    }
                )

                DropdownMenu(
                    expanded = state.isAreaSheetVisible && state.areas.isNotEmpty(),
                    onDismissRequest = { listener.onToggleAreaSheet(false) },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    state.areas.forEach { area ->
                        DropdownMenuItem(
                            text = { Text(area, style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurface) },
                            onClick = {
                                listener.onSelectArea(area)
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.floor,
                    onValueChange = listener::onFloorChange,
                    label = { Text(stringResource(Res.string.floor), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    isError = state.floorError != null,
                    supportingText = state.floorError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.apartment,
                    onValueChange = listener::onApartmentChange,
                    label = { Text(stringResource(Res.string.apartment), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = state.specialMark,
                onValueChange = listener::onSpecialMarkChange,
                label = { Text(stringResource(Res.string.special_mark), style = Theme.typography.bodyMedium, color = Theme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.specialMarkError != null,
                supportingText = state.specialMarkError?.let { { Text(it.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                singleLine = true
            )
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
private fun RegisterStep2ContentPreviewLightDark() {
    var state by remember { mutableStateOf(RegisterScreenState(currentStep = 2)) }
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
            override fun onPhoneChange(value: String) { state = state.copy(phone = value) }
            override fun onHomePhoneChange(value: String) { state = state.copy(homePhone = value) }
            override fun onEmailChange(value: String) { state = state.copy(email = value) }
            override fun onPasswordChange(value: String) { state = state.copy(password = value) }
            override fun onTogglePasswordVisibility() { state = state.copy(isPasswordVisible = !state.isPasswordVisible) }
            override fun onBuildingNoChange(value: String) { state = state.copy(buildingNo = value) }
            override fun onStreetChange(value: String) { state = state.copy(street = value) }
            override fun onStreetBranchChange(value: String) { state = state.copy(streetBranch = value) }
            override fun onAreaChange(value: String) { state = state.copy(selectedArea = value) }
            override fun onSelectArea(area: String) { state = state.copy(selectedArea = area) }
            override fun onToggleAreaSheet(visible: Boolean) { state = state.copy(isAreaSheetVisible = visible) }
            override fun onFloorChange(value: String) { state = state.copy(floor = value) }
            override fun onApartmentChange(value: String) { state = state.copy(apartment = value) }
            override fun onSpecialMarkChange(value: String) { state = state.copy(specialMark = value) }
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
            RegisterStep2Content(state = state, listener = listener)
        }
    }
}
