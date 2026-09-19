package com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.asString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.create_new_password
import teecclesia.designsystem.generated.resources.deleted_account_found
import teecclesia.designsystem.generated.resources.enter_code_sent_on_your_email
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.ic_profile
import teecclesia.designsystem.generated.resources.login
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.reactivate_account_reset_password_notice

@Composable
fun CreateNewPasswordScreen(
    key: String,
    otp: String,
    isPhone: Boolean,
    isDeletedAccount: Boolean,
    viewModel: CreateNewPasswordViewModel = koinViewModel(
        parameters = { parametersOf(key, otp, isPhone, isDeletedAccount) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CreateNewPasswordContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun CreateNewPasswordContent(
    state: CreateNewPasswordUiState,
    interactionListener: CreateNewPasswordInteractionListener
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
                text = stringResource(Res.string.create_new_password),
                style = Theme.typography.headlineLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Text(
                text = stringResource(Res.string.enter_code_sent_on_your_email),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = if (state.isDeletedAccount) 16.dp else 32.dp)
            )
        }

        if (state.isDeletedAccount) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Theme.colorScheme.surfaceContainerHighest,
                    border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_profile),
                                contentDescription = null,
                                tint = Theme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(Res.string.deleted_account_found),
                                style = Theme.typography.titleMedium,
                                color = Theme.colorScheme.primary
                            )
                        }
                        Text(
                            text = stringResource(Res.string.reactivate_account_reset_password_notice),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            CustomTextField(
                value = state.password,
                onValueChange = interactionListener::onPasswordChange,
                labelText = stringResource(Res.string.password),
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).testTag("PasswordInput"),
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
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = interactionListener::onLoginClicked,
                    modifier = Modifier.fillMaxWidth().testTag("LoginButton"),
                    text = stringResource(Res.string.login),
                    state = state.actionButtonState
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateNewPasswordScreenPreview() = Theme {
    CreateNewPasswordContent(
        state = CreateNewPasswordUiState(
            key = "user",
            otp = "1234",
            isDeletedAccount = true
        ),
        interactionListener = object : CreateNewPasswordInteractionListener {
            override fun onPasswordChange(value: String) {}
            override fun onTogglePasswordVisibility() {}
            override fun onLoginClicked() {}
        }
    )
}
