package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.identity.presentation.shared.components.AddressFieldsSection
import com.teEcclesia.identity.presentation.shared.components.ContactInfoFields
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
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.address_info
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.invalid_password_format
import teecclesia.designsystem.generated.resources.account_and_contact

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

            ContactInfoFields(
                phone = state.phone,
                onPhoneChange = listener::onPhoneChange,
                phoneError = state.phoneError?.asString(),
                homePhone = state.homePhone,
                onHomePhoneChange = listener::onHomePhoneChange,
                homePhoneError = state.homePhoneError?.asString(),
                email = state.email,
                onEmailChange = listener::onEmailChange,
                emailError = state.emailError?.asString()
            )

            CustomTextField(
                value = state.password,
                onValueChange = listener::onPasswordChange,
                labelText = stringResource(Res.string.password),
                supportingText = stringResource(Res.string.invalid_password_format),
                modifier = Modifier.fillMaxWidth(),
                errorText = state.passwordError?.asString(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = painterResource(if (state.isPasswordVisible) Res.drawable.ic_eye_closed else Res.drawable.ic_eye_opened),
                onTrailingIconClick = listener::onTogglePasswordVisibility
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.address_info),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            AddressFieldsSection(
                buildingNo = state.buildingNo,
                onBuildingNoChange = listener::onBuildingNoChange,
                buildingNoError = state.buildingNoError?.asString(),
                street = state.street,
                onStreetChange = listener::onStreetChange,
                streetError = state.streetError?.asString(),
                streetBranch = state.streetBranch,
                onStreetBranchChange = listener::onStreetBranchChange,
                area = state.selectedArea ?: "",
                onAreaChange = listener::onAreaChange,
                areas = state.areas,
                isAreaSheetVisible = state.isAreaSheetVisible,
                onToggleAreaSheet = listener::onToggleAreaSheet,
                onSelectArea = listener::onSelectArea,
                areaError = state.areaError?.asString(),
                floor = state.floor,
                onFloorChange = listener::onFloorChange,
                floorError = state.floorError?.asString(),
                apartment = state.apartment,
                onApartmentChange = listener::onApartmentChange,
                specialMark = state.specialMark,
                onSpecialMarkChange = listener::onSpecialMarkChange,
                specialMarkError = state.specialMarkError?.asString()
            )
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
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterStep2Content(state = state, listener = listener)
        }
    }
}
