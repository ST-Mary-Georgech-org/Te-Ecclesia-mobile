package com.teEcclesia.identity.presentation.screen.verifyPhone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.indicator.DotsProgressIndicator
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.OtpInputField
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.designsystem.util.formatTime
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.shared.components.ScreenTemplate
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.didnt_receive_code
import teecclesia.designsystem.generated.resources.resend
import teecclesia.designsystem.generated.resources.verify_your_phone

@Composable
fun VerifyPhoneScreen(
    viewModel: VerifyPhoneViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    VerifyPhoneScreenContent(
        state = state,
        interactionListener = viewModel,
    )
}

@Composable
private fun VerifyPhoneScreenContent(
    state: VerifyPhoneUiState,
    interactionListener: VerifyPhoneInteractionListener,
) {
    ScreenTemplate(
        title = Res.string.verify_your_phone.asString(),
        onClickActionButton = {},
        actionButtonText = null,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        underActionButtonContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val animatedTextColor by animateColorAsState(
                    targetValue = if (state.canResend && !state.isLoading) Theme.colorScheme.button.primary
                    else Theme.colorScheme.text.label
                )
                Text(
                    text = Res.string.didnt_receive_code.asString(),
                    color = Theme.colorScheme.text.link,
                    style = Theme.typography.label.medium.medium,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = Res.string.resend.asString(),
                    color = animatedTextColor,
                    style = Theme.typography.label.semiBold.medium,
                    modifier = Modifier.clickable(
                        onClick = interactionListener::onResendClicked,
                        enabled = state.canResend && !state.isLoading
                    )
                )
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OtpInputField(
                otpText = state.otp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
                otpLength = 4,
                errorText = state.otpError?.asString(),
                spaceBetweenCharacters = 12.dp,
                onOtpModified = interactionListener::onOtpChange,
            )
            Text(
                text = formatTime(state.timeRemaining),
                style = Theme.typography.body.medium.copy(
                    color = Theme.colorScheme.brand.primary
                ),
                modifier = Modifier
                    .align(Alignment.End)
            )
            AnimatedVisibility(
                visible = state.isLoading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                DotsProgressIndicator(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun VerifyPhoneScreenPreview() = TeEcclesiaTheme {
    VerifyPhoneScreenContent(
        state = VerifyPhoneUiState(
            canResend = true,
            isLoading = true
        ),
        interactionListener = object : VerifyPhoneInteractionListener {
            override fun onOtpChange(newOtp: String) {}
            override fun onResendClicked() {}
        }
    )
}
