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
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.identity.presentation.shared.components.AddressFieldsSection
import com.teEcclesia.identity.presentation.shared.components.ConfessionPriestField
import com.teEcclesia.identity.presentation.shared.components.ContactInfoFields
import com.teEcclesia.identity.presentation.shared.components.FourNamesFields
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.AvatarPicker
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
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
import teecclesia.designsystem.generated.resources.ic_home_mark
import teecclesia.designsystem.generated.resources.ic_profile_mark
import teecclesia.designsystem.generated.resources.job
import teecclesia.designsystem.generated.resources.job_hint
import teecclesia.designsystem.generated.resources.national_id
import teecclesia.designsystem.generated.resources.national_id_hint
import teecclesia.designsystem.generated.resources.next_step
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
