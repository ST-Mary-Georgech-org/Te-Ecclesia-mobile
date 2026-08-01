package com.teEcclesia.identity.presentation.screen.resetPassword.verifyPhone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.verify_button
import teecclesia.designsystem.generated.resources.verify_phone_reset_subtitle
import teecclesia.designsystem.generated.resources.verify_your_phone

@Composable
fun VerifyPhoneResetPasswordScreen(
    phone: String,
    token: String,
    link: String,
    viewModel: VerifyPhoneResetPasswordViewModel = koinViewModel(parameters = { parametersOf(phone, token, link) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    VerifyPhoneResetPasswordContent(
        state = state,
        interactionListener = object : VerifyPhoneResetPasswordInteractionListener {
            override fun onVerifyClicked() {
                viewModel.onVerifyClicked()
                if (state.link.isNotBlank()) {
                    uriHandler.openUri(state.link)
                }
            }

            override fun onNextClicked() {
                viewModel.onNextClicked()
            }
        }
    )
}

@Composable
fun VerifyPhoneResetPasswordContent(
    state: VerifyPhoneResetPasswordUiState,
    interactionListener: VerifyPhoneResetPasswordInteractionListener
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(Res.string.verify_your_phone),
                style = Theme.typography.headlineLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Text(
                text = stringResource(Res.string.verify_phone_reset_subtitle),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = interactionListener::onVerifyClicked,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.verify_button),
                    state = state.actionButtonState
                )

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = interactionListener::onNextClicked,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.next),
                    state = state.actionButtonState
                )
            }
        }
    }
}

@Preview
@Composable
private fun VerifyPhoneResetPasswordScreenPreview() = Theme {
    VerifyPhoneResetPasswordContent(
        state = VerifyPhoneResetPasswordUiState(
            phone = "01000000000",
            token = "1234",
            link = "https://wa.me/..."
        ),
        interactionListener = object : VerifyPhoneResetPasswordInteractionListener {
            override fun onVerifyClicked() {}
            override fun onNextClicked() {}
        }
    )
}
