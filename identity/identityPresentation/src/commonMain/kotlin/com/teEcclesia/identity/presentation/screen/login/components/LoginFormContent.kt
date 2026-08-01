package com.teEcclesia.identity.presentation.screen.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import org.jetbrains.compose.resources.stringResource
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.login.LoginInteractionListener
import com.teEcclesia.identity.presentation.screen.login.LoginScreenState
import com.teEcclesia.shared.domain.utils.validation.isValidPhoneInput
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.dont_have_an_account
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.forget_your_password
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.login
import teecclesia.designsystem.generated.resources.phone_number
import teecclesia.designsystem.generated.resources.phone_number_supporting_text
import teecclesia.designsystem.generated.resources.register

@Composable
fun LoginFormContent(
    state: LoginScreenState,
    interactionListener: LoginInteractionListener
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
            val isPhoneInput = isValidPhoneInput(state.username)

            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                CustomTextField(
                    value = state.username,
                    onValueChange = interactionListener::onUsernameChange,
                    labelText = stringResource(Res.string.phone_number),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = state.usernameError?.asString(),
                    supportingText = stringResource(Res.string.phone_number_supporting_text),
                    textStyle = if (isPhoneInput) {
                        Theme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr)
                    } else {
                        Theme.typography.bodyLarge
                    },
                    prefixText = if (isPhoneInput && !isRtl) { "+2" } else null,
                    suffixText = if (isPhoneInput && isRtl) { "+2" } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                )
            }

            CustomTextField(
                value = state.password,
                onValueChange = interactionListener::onPasswordChange,
                labelText = stringResource(Res.string.password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                errorText = state.passwordError?.asString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = when (state.isPasswordVisible) {
                    true -> Res.drawable.ic_eye_closed.painter()
                    false -> Res.drawable.ic_eye_opened.painter()
                },
                onTrailingIconClick = interactionListener::onTogglePasswordVisibility
            )

            Text(
                text = stringResource(Res.string.forget_your_password),
                color = Theme.colorScheme.primary,
                style = Theme.typography.labelMedium,
                modifier = Modifier.clickable(onClick = interactionListener::onForgotPasswordClicked)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                type = AppButtonType.Primary,
                onClick = interactionListener::onLoginClicked,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.login),
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
