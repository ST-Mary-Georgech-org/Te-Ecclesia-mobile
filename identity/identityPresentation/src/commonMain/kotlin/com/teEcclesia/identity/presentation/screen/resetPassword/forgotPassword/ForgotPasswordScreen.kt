package com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.shared.domain.utils.validation.isValidPhoneInput
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.back_to_login_question
import teecclesia.designsystem.generated.resources.dont_have_an_account
import teecclesia.designsystem.generated.resources.forgot_password_subtitle
import teecclesia.designsystem.generated.resources.forgot_password_title
import teecclesia.designsystem.generated.resources.logo
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.phone_or_verified_email
import teecclesia.designsystem.generated.resources.phone_or_verified_email_supporting_text
import teecclesia.designsystem.generated.resources.register

import org.koin.core.parameter.parametersOf

@Composable
fun ForgotPasswordScreen(
    initialKey: String = "",
    viewModel: ForgotPasswordViewModel = koinViewModel(parameters = { parametersOf(initialKey) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ForgotPasswordContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun ForgotPasswordContent(
    state: ForgotPasswordUiState,
    interactionListener: ForgotPasswordInteractionListener
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val isPhoneInput = isValidPhoneInput(state.identifier)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.forgot_password_title),
                        style = Theme.typography.headlineLarge,
                        color = Theme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        item {
            Text(
                text = stringResource(Res.string.forgot_password_subtitle),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomTextField(
                        value = state.identifier,
                        onValueChange = interactionListener::onIdentifierChange,
                        labelText = stringResource(Res.string.phone_or_verified_email),
                        modifier = Modifier.fillMaxWidth(),
                        errorText = state.identifierError?.asString(),
                        supportingText = stringResource(Res.string.phone_or_verified_email_supporting_text),
                        textStyle = if (isPhoneInput) {
                            Theme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr)
                        } else {
                            Theme.typography.bodyLarge
                        },
                        prefixText = if (isPhoneInput && !isRtl) "+2" else null,
                        suffixText = if (isPhoneInput && isRtl) "+2" else null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Done
                        )
                    )
                }

                Text(
                    text = stringResource(Res.string.back_to_login_question),
                    color = Theme.colorScheme.primary,
                    style = Theme.typography.labelMedium,
                    modifier = Modifier.clickable(onClick = interactionListener::onBackToLoginClicked)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = interactionListener::onNextClicked,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.next),
                    state = state.actionButtonState
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.dont_have_an_account),
                        color = Theme.colorScheme.onSurfaceVariant,
                        style = Theme.typography.labelMedium
                    )
                    Text(
                        text = stringResource(Res.string.register),
                        color = Theme.colorScheme.primary,
                        style = Theme.typography.labelLarge,
                        modifier = Modifier.clickable(onClick = interactionListener::onSignUpClicked)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ForgotPasswordScreenPreview() = Theme {
    ForgotPasswordContent(
        state = ForgotPasswordUiState(),
        interactionListener = object : ForgotPasswordInteractionListener {
            override fun onIdentifierChange(value: String) {}
            override fun onNextClicked() {}
            override fun onBackToLoginClicked() {}
            override fun onSignUpClicked() {}
        }
    )
}
