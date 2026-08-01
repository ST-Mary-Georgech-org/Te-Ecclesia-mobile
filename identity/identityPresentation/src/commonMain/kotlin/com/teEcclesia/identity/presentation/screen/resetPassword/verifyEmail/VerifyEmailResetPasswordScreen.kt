package com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.OtpInputField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.didnt_receive_code
import teecclesia.designsystem.generated.resources.resend
import teecclesia.designsystem.generated.resources.verify_code
import teecclesia.designsystem.generated.resources.verify_email_reset_subtitle
import teecclesia.designsystem.generated.resources.verify_email_title

@Composable
fun VerifyEmailResetPasswordScreen(
    email: String,
    viewModel: VerifyEmailResetPasswordViewModel = koinViewModel(parameters = { parametersOf(email) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    VerifyEmailResetPasswordContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun VerifyEmailResetPasswordContent(
    state: VerifyEmailResetPasswordUiState,
    interactionListener: VerifyEmailResetPasswordInteractionListener
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
                text = stringResource(Res.string.verify_email_title),
                style = Theme.typography.headlineLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Text(
                text = stringResource(Res.string.verify_email_reset_subtitle),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        item {
            OtpInputField(
                otpText = state.otpCode,
                otpLength = 4,
                errorText = state.otpError?.asString(),
                onOtpModified = interactionListener::onOtpChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = interactionListener::onVerifyCodeClicked,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.verify_code),
                    state = state.actionButtonState
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.didnt_receive_code),
                        color = Theme.colorScheme.onSurfaceVariant,
                        style = Theme.typography.labelMedium
                    )
                    Text(
                        text = stringResource(Res.string.resend),
                        color = Theme.colorScheme.primary,
                        style = Theme.typography.labelLarge,
                        modifier = Modifier.clickable(onClick = interactionListener::onResendClicked)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun VerifyEmailResetPasswordScreenPreview() = Theme {
    VerifyEmailResetPasswordContent(
        state = VerifyEmailResetPasswordUiState(
            email = "test@example.com",
            otpCode = "1810"
        ),
        interactionListener = object : VerifyEmailResetPasswordInteractionListener {
            override fun onOtpChange(value: String) {}
            override fun onVerifyCodeClicked() {}
            override fun onResendClicked() {}
        }
    )
}
