package com.teEcclesia.identity.presentation.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.shared.components.ScreenTemplate
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.dont_have_an_account
import teecclesia.designsystem.generated.resources.enter_your_password
import teecclesia.designsystem.generated.resources.enter_your_username
import teecclesia.designsystem.generated.resources.enter_your_username_and_password
import teecclesia.designsystem.generated.resources.forget_your_password
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.login
import teecclesia.designsystem.generated.resources.signup

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoginScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun LoginScreenContent(
    state: LoginScreenState,
    interactionListener: LoginInteractionListener
) {
    ScreenTemplate(
        title = Res.string.login.asString(),
        subtitle = Res.string.enter_your_username_and_password.asString(),
        actionButtonState = state.actionButtonState,
        onClickActionButton = interactionListener::onLoginClicked,
        actionButtonText = Res.string.login.asString(),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        underActionButtonContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Res.string.dont_have_an_account.asString(),
                    color = Theme.colorScheme.text.label,
                    style = Theme.typography.label.medium.medium,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = Res.string.signup.asString(),
                    modifier = Modifier.clickable(onClick = interactionListener::onSignUpClicked),
                    style = Theme.typography.label.semiBold.medium,
                    color = Theme.colorScheme.text.title,
                )
            }
        },
    ) {

        CustomTextField(
            value = state.username,
            onValueChange = interactionListener::onUsernameChange,
            hint = Res.string.enter_your_username.asString(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            errorText = state.usernameError?.asString(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            ),
        )

        CustomTextField(
            value = state.password,
            onValueChange = interactionListener::onPasswordChange,
            hint = Res.string.enter_your_password.asString(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
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
            trailingIconColor = Theme.colorScheme.text.label,
            onTrailingIconClick = interactionListener::onTogglePasswordVisibility,
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = Res.string.forget_your_password.asString(),
                color = Theme.colorScheme.text.label,
                modifier = Modifier
                    .clickable(onClick = interactionListener::onForgotPasswordClicked),
                style = Theme.typography.label.medium.medium
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() = TeEcclesiaTheme {
    LoginScreenContent(
        state = LoginScreenState(
            username = "",
            password = "",
            usernameError = null,
            passwordError = null,
            isPasswordVisible = false,
            actionButtonState = AppButtonState.Enabled
        ),
        interactionListener = object : LoginInteractionListener {
            override fun onLoginClicked() {}
            override fun onSignUpClicked() {}
            override fun onForgotPasswordClicked() {}
            override fun onUsernameChange(newUsername: String) {}
            override fun onPasswordChange(newPassword: String) {}
            override fun onTogglePasswordVisibility() {}
        }
    )
}
