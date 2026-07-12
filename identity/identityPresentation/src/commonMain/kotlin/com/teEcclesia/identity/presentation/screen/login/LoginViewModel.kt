package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.useCase.validation.auth.ValidationUseCase
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.designsystem.navigation.BaseViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.invalid_password
import teecclesia.designsystem.generated.resources.invalid_username
import teecclesia.designsystem.generated.resources.unknown_error

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val validationUseCase: ValidationUseCase
) : BaseViewModel<LoginScreenState>(LoginScreenState()), LoginInteractionListener {

    override fun onLoginClicked() {
        validateFields()
        if (state.value.usernameError != null || state.value.passwordError != null) return

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                authenticationRepository.login(
                    username = state.value.username,
                    password = state.value.password
                )
            },
            onSuccess = { },
            onError = { error ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.error_occurred),
                    message = error.message?.let { UiText.DynamicString(it) }
                        ?: UiText.StringRes(Res.string.unknown_error),
                    isSuccess = false,
                )
            },
            onEnd = {
                updateState { copy(actionButtonState = AppButtonState.Enabled) }
            }
        )
    }

    private fun validateFields() {
        validateUsername()
        validatePassword()
        if (state.value.usernameError == null && state.value.passwordError == null) {
            updateState { copy(actionButtonState = AppButtonState.Enabled) }
        }
    }

    private fun validateUsername() {
        when (validationUseCase.validateUsername(state.value.username)) {
            true -> updateState { copy(usernameError = null) }
            else -> updateState { copy(usernameError = UiText.StringRes(Res.string.invalid_username)) }
        }
    }

    private fun validatePassword() {
        when (validationUseCase.validatePassword(state.value.password)) {
            true -> updateState { copy(passwordError = null) }
            else -> updateState { copy(passwordError = UiText.StringRes(Res.string.invalid_password)) }
        }
    }

    override fun onSignUpClicked() {
        navigate(SignUpRoute)
    }

    override fun onForgotPasswordClicked() {}

    override fun onUsernameChange(newUsername: String) {
        updateState { copy(username = newUsername.replace(Regex("[^A-Za-z0-9_]"), "")) }
        validateUsername()
    }

    override fun onPasswordChange(newPassword: String) {
        updateState { copy(password = newPassword) }
        validatePassword()
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }
}
