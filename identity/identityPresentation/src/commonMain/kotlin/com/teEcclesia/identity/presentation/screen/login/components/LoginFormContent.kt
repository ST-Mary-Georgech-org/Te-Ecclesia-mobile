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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.Button
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.OutlinedTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.login.LoginInteractionListener
import com.teEcclesia.identity.presentation.screen.login.LoginScreenState
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
            val isPhoneInput = state.username.matches("^01[0125][0-9]{0,9}$".toRegex()) && state.username.length in 1..11

            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                OutlinedTextField(
                    value = state.username,
                    onValueChange = interactionListener::onUsernameChange,
                    label = {
                        Text(
                            Res.string.phone_number.asString(),
                            style = Theme.typography.bodyLarge,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.usernameError != null,
                    supportingText = {
                        if (state.usernameError != null) {
                            Text(state.usernameError.asString(), style = Theme.typography.bodySmall, color = Theme.colorScheme.error)
                        } else {
                            Text(
                                Res.string.phone_number_supporting_text.asString(),
                                style = Theme.typography.bodySmall,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    textStyle = if (isPhoneInput) {
                        Theme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr)
                    } else {
                        Theme.typography.bodyLarge
                    },
                    prefix = if (isPhoneInput && !isRtl) {
                        { Text("+2", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurfaceVariant) }
                    } else null,
                    suffix = if (isPhoneInput && isRtl) {
                        { Text("2+", style = Theme.typography.bodyLarge, color = Theme.colorScheme.onSurfaceVariant) }
                    } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii,
                        imeAction = ImeAction.Next
                    ),
                )
            }

            OutlinedTextField(
                value = state.password,
                onValueChange = interactionListener::onPasswordChange,
                label = {
                    Text(
                        Res.string.password.asString(),
                        style = Theme.typography.bodyLarge,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let {
                    {
                        Text(
                            it.asString(),
                            style = Theme.typography.bodySmall,
                            color = Theme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Icon(
                        painter = when (state.isPasswordVisible) {
                            true -> Res.drawable.ic_eye_closed.painter()
                            false -> Res.drawable.ic_eye_opened.painter()
                        },
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable(onClick = interactionListener::onTogglePasswordVisibility)
                    )
                }
            )

            Text(
                text = Res.string.forget_your_password.asString(),
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
            Button(
                onClick = interactionListener::onLoginClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Theme.colorScheme.primary,
                contentColor = Theme.colorScheme.onPrimary,
                enabled = state.actionButtonState == AppButtonState.Enabled
            ) {
                Text(
                    text = Res.string.login.asString(),
                    style = Theme.typography.labelLarge,
                    color = Theme.colorScheme.onPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Res.string.dont_have_an_account.asString(),
                    color = Theme.colorScheme.onSurfaceVariant,
                    style = Theme.typography.labelMedium
                )
                Text(
                    text = Res.string.register.asString(),
                    color = Theme.colorScheme.primary,
                    style = Theme.typography.labelLarge,
                    modifier = Modifier.clickable(onClick = interactionListener::onSignUpClicked)
                )
            }
        }
    }
}
