package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.AvatarPicker
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.AddressFieldsSection
import com.teEcclesia.identity.presentation.shared.components.ConfessionPriestField
import com.teEcclesia.identity.presentation.shared.components.ContactInfoFields
import com.teEcclesia.identity.presentation.shared.components.FourNamesFields
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.account_contact_info
import teecclesia.designsystem.generated.resources.address_info
import teecclesia.designsystem.generated.resources.code
import teecclesia.designsystem.generated.resources.confession_priest
import teecclesia.designsystem.generated.resources.display_name
import teecclesia.designsystem.generated.resources.display_name_hint
import teecclesia.designsystem.generated.resources.ic_church_mark
import teecclesia.designsystem.generated.resources.ic_contact
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.ic_home_mark
import teecclesia.designsystem.generated.resources.ic_profile_mark
import teecclesia.designsystem.generated.resources.job
import teecclesia.designsystem.generated.resources.job_hint
import teecclesia.designsystem.generated.resources.national_id
import teecclesia.designsystem.generated.resources.national_id_hint
import teecclesia.designsystem.generated.resources.next_step
import teecclesia.designsystem.generated.resources.optional_password_hint
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.personal_info
import teecclesia.designsystem.generated.resources.should_have_whatsapp

@Composable
fun ReviewStep1Content(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.personal_info),
            icon = painterResource(Res.drawable.ic_profile_mark)
        ) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AvatarPicker(
                    imageBytes = state.imageBytes,
                    imageUrl = state.imageUrl,
                    onClick = { listener.onClickUpload(UploadTarget.PROFILE_PHOTO) }
                )
            }

            CustomTextField(
                value = state.code,
                onValueChange = listener::onCodeChanged,
                labelText = stringResource(Res.string.code),
                errorText = state.codeError?.asString(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            FourNamesFields(
                firstName = state.firstName,
                onFirstNameChange = listener::onFirstNameChanged,
                firstNameError = state.firstNameError?.asString(),
                secondName = state.secondName,
                onSecondNameChange = listener::onSecondNameChanged,
                secondNameError = state.secondNameError?.asString(),
                thirdName = state.thirdName,
                onThirdNameChange = listener::onThirdNameChanged,
                thirdNameError = state.thirdNameError?.asString(),
                lastName = state.lastName,
                onLastNameChange = listener::onLastNameChanged,
                lastNameError = state.lastNameError?.asString()
            )

            CustomTextField(
                value = state.displayName,
                onValueChange = listener::onDisplayNameChanged,
                labelText = stringResource(Res.string.display_name),
                supportingText = stringResource(Res.string.display_name_hint),
                errorText = state.displayNameError?.asString(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                allowEmojis = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            CustomTextField(
                value = state.nationalId,
                onValueChange = listener::onNationalIdChanged,
                labelText = stringResource(Res.string.national_id),
                supportingText = stringResource(Res.string.national_id_hint),
                errorText = state.nationalIdError?.asString(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            CustomTextField(
                value = state.job,
                onValueChange = listener::onJobChanged,
                labelText = stringResource(Res.string.job),
                supportingText = stringResource(Res.string.job_hint),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        ReviewSectionCard(
            title = stringResource(Res.string.confession_priest),
            icon = painterResource(Res.drawable.ic_church_mark)
        ) {
            ConfessionPriestField(
                selectedPriestName = state.selectedConfessionPriest?.name ?: state.confessionPriestName,
                isFromAnotherChurch = state.isFromAnotherChurch,
                isPriestSheetVisible = state.isPriestSheetVisible,
                confessionPriests = state.confessionPriests,
                confessionPriestError = state.confessionPriestError?.asString(),
                externalPriestName = state.confessionPriestName,
                externalPriestNameError = state.externalPriestNameError?.asString(),
                externalPriestChurch = state.confessionPriestChurch,
                externalPriestChurchError = state.externalPriestChurchError?.asString(),
                externalPriestPhone = state.confessionPriestPhone,
                externalPriestPhoneError = state.externalPriestPhoneError?.asString(),
                onTogglePriestSheet = listener::onTogglePriestSheet,
                onSelectConfessionPriest = listener::onSelectConfessionPriest,
                onSelectFromAnotherChurch = listener::onSelectFromAnotherChurch,
                onLoadNextPriests = listener::onLoadNextPriests,
                onExternalPriestNameChange = listener::onConfessionPriestNameChanged,
                onExternalPriestChurchChange = listener::onConfessionPriestChurchChanged,
                onExternalPriestPhoneChange = listener::onConfessionPriestPhoneChanged
            )
        }

        ReviewSectionCard(
            title = stringResource(Res.string.account_contact_info),
            icon = painterResource(Res.drawable.ic_contact)
        ) {
            ContactInfoFields(
                phone = state.phone,
                onPhoneChange = listener::onPhoneChanged,
                phoneError = state.phoneError?.asString(),
                homePhone = state.homePhone,
                onHomePhoneChange = listener::onHomePhoneChanged,
                homePhoneError = state.homePhoneError?.asString(),
                email = state.email,
                onEmailChange = listener::onEmailChanged,
                emailError = state.emailError?.asString(),
                supportingTextPhone = stringResource(Res.string.should_have_whatsapp)
            )
            
            if (state.isUpdateMode) {
                CustomTextField(
                    value = state.password,
                    onValueChange = listener::onPasswordChanged,
                    labelText = stringResource(Res.string.password),
                    supportingText = stringResource(Res.string.optional_password_hint),
                    errorText = state.passwordError?.asString(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = when (state.isPasswordVisible) {
                        true -> painterResource(Res.drawable.ic_eye_closed)
                        false -> painterResource(Res.drawable.ic_eye_opened)
                    },
                    onTrailingIconClick = listener::onTogglePasswordVisibility
                )
            }
        }

        ReviewSectionCard(
            title = stringResource(Res.string.address_info),
            icon = painterResource(Res.drawable.ic_home_mark)
        ) {
            AddressFieldsSection(
                buildingNo = state.buildingNo,
                onBuildingNoChange = listener::onBuildingNoChanged,
                buildingNoError = state.buildingNoError?.asString(),
                street = state.street,
                onStreetChange = listener::onStreetChanged,
                streetError = state.streetError?.asString(),
                streetBranch = state.streetBranch,
                onStreetBranchChange = listener::onStreetBranchChanged,
                area = state.area,
                onAreaChange = listener::onAreaChanged,
                areas = state.areas,
                isAreaSheetVisible = state.isAreaSheetVisible,
                onToggleAreaSheet = listener::onToggleAreaSheet,
                onSelectArea = listener::onSelectArea,
                areaError = state.areaError?.asString(),
                floor = state.floor,
                onFloorChange = listener::onFloorChanged,
                floorError = state.floorError?.asString(),
                apartment = state.apartment,
                onApartmentChange = listener::onApartmentChanged,
                specialMark = state.specialMark,
                onSpecialMarkChange = listener::onSpecialMarkChanged,
                specialMarkError = state.specialMarkError?.asString()
            )
        }

        AppButton(
            text = stringResource(Res.string.next_step),
            onClick = { listener.onNextStep() },
            type = AppButtonType.Primary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
private fun ReviewStep1ContentPreview() = Theme {
    val state = ReviewAndEditRequestUiState(
        userId = "123",
        isLoading = false
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

        override fun onSelectImageBytes(
            target: UploadTarget,
            bytes: ByteArray?,
            fileName: String?
        ) {
        }

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
        override fun onToggleMotherDeceased(deceased: Boolean) {}
        override fun onMotherPhoneChange(value: String) {}
        override fun onMotherWhatsappChange(value: String) {}

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
        override fun onLoadNextRanks() {}

        override fun onNotesChanged(value: String) {}
    }

    Preview {
        ReviewStep1Content(
            state = state,
            listener = listener,
        )
    }
}