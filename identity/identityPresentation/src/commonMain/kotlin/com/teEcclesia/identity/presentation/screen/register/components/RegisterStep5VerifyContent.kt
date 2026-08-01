package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.MultiHighlightedClickableText
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.text.TextSegment
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.preview.PreviewAppModes
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.RegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.register.RegisterScreenState
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.verify_your_phone
import teecclesia.designsystem.generated.resources.verify_whatsapp
import teecclesia.designsystem.generated.resources.check_verification_status
import teecclesia.designsystem.generated.resources.click_verify_button_to_send_a_whatsapp_message_to_verify_the_number_after_accepting_your_registration_request_we_will_contact_you
import teecclesia.designsystem.generated.resources.go_to_login
import teecclesia.designsystem.generated.resources.if_the_whatsapp_number_in_another_phone_register_from_that_phone_first

@Composable
fun RegisterStep5VerifyContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.verify_your_phone),
            style = Theme.typography.displaySmall,
            color = Theme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.click_verify_button_to_send_a_whatsapp_message_to_verify_the_number_after_accepting_your_registration_request_we_will_contact_you),
            style = Theme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth(),
            color = Theme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                type = AppButtonType.Primary,
                onClick = {
                    state.whatsAppDeepLink?.let { uriHandler.openUri(it) }
                    listener.onClickVerifyWhatsApp()
                },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.verify_whatsapp)
            )

            AppButton(
                type = AppButtonType.Secondary,
                onClick = listener::onClickCheckWhatsAppStatus,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.check_verification_status),
                state = state.actionButtonState
            )
        }
        MultiHighlightedClickableText(
            segments = listOf(
                TextSegment.Normal(stringResource(Res.string.if_the_whatsapp_number_in_another_phone_register_from_that_phone_first)),
                TextSegment.Highlighted(
                    text = stringResource(Res.string.go_to_login),
                    onClick = listener::onClickLogin
                )
            ),
            textAlign = TextAlign.Start,
            textStyle = Theme.typography.labelMedium
        )
    }
}

@PreviewAppModes
@Composable
private fun RegisterStep5VerifyContentPreviewLightDark() {
    var state by remember { mutableStateOf(RegisterScreenState(currentStep = 5)) }
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
            RegisterStep5VerifyContent(state = state, listener = listener)
        }
    }
}
