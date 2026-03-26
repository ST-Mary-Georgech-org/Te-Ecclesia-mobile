package com.teEcclesia.identity.presentation.screen.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.shared.components.ScreenTemplate
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.already_have_an_account
import teecclesia.designsystem.generated.resources.create_account
import teecclesia.designsystem.generated.resources.enter_your_name
import teecclesia.designsystem.generated.resources.enter_your_password
import teecclesia.designsystem.generated.resources.enter_your_phone
import teecclesia.designsystem.generated.resources.enter_your_username
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.login
import teecclesia.designsystem.generated.resources.signup

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SignUpScreenContent(
        interactionListener = viewModel,
        state = state
    )
}

@Composable
private fun SignUpScreenContent(
    interactionListener: SignUpInteractionListener,
    state: SignUpUiState
) {
    ScreenTemplate(
        title = Res.string.create_account.asString(),
        actionButtonState = state.actionButtonState,
        onClickActionButton = interactionListener::onSignUpClicked,
        actionButtonText = Res.string.signup.asString(),
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
                Text(
                    text = Res.string.already_have_an_account.asString(),
                    color = Theme.colorScheme.text.link,
                    style = Theme.typography.label.medium.medium,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = Res.string.login.asString(),
                    modifier = Modifier.clickable(onClick = interactionListener::onLoginClicked),
                    style = Theme.typography.label.semiBold.medium,
                    color = Theme.colorScheme.text.title,
                )
            }
        }
    ) {
        CustomTextField(
            value = state.fullName,
            onValueChange = interactionListener::onNameChange,
            hint = Res.string.enter_your_name.asString(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            errorText = state.fullNameError?.asString(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        CustomTextField(
            value = state.username,
            onValueChange = interactionListener::onChangeUsername,
            hint = Res.string.enter_your_username.asString(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            errorText = state.userNameError?.asString(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            )
        )
        CustomTextField(
            value = state.phone,
            onValueChange = interactionListener::onPhoneChange,
            hint = Res.string.enter_your_phone.asString(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            errorText = state.phoneError?.asString(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            )
        )
        CustomTextField(
            value = state.password,
            onValueChange = interactionListener::onPasswordChange,
            hint = Res.string.enter_your_password.asString(),
            modifier = Modifier.fillMaxWidth(),
            errorText = state.passwordError?.asString(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (state.isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = when (state.isPasswordVisible) {
                true -> Res.drawable.ic_eye_closed.painter()
                false -> Res.drawable.ic_eye_opened.painter()
            },
            trailingIconColor = Theme.colorScheme.text.label,
            onTrailingIconClick = interactionListener::onTogglePasswordVisibility,
        )
    }
}


@Composable
@Preview
fun SignUpScreenPreview() = TeEcclesiaTheme {
    SignUpScreenContent(
        interactionListener = object : SignUpInteractionListener {
            override fun onSignUpClicked() {}
            override fun onLoginClicked() {}
            override fun onNameChange(newName: String) {}
            override fun showDatePicker() {}
            override fun onChangeUsername(newUsername: String) {}
            override fun onDismissDatePicker() {}
            override fun onPhoneChange(newPhone: String) {}
            override fun onPasswordChange(newPassword: String) {}
            override fun onTogglePasswordVisibility() {}
            override fun onTermsAndConditionsClicked() {}
            override fun onPrivacyPolicyClicked() {}
            override fun onTermsAndConditionsBottomSheetDismissed() {}
            override fun onPrivacyPolicyBottomSheetDismissed() {}
        },
        state = SignUpUiState()
    )
}