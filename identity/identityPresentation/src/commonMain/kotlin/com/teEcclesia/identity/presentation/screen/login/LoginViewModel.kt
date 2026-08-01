package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.identity.api.ForgotPasswordRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.LoginRequest
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.presentation.util.toUiText
import com.teEcclesia.shared.domain.utils.validation.getPasswordValidationError
import com.teEcclesia.shared.domain.utils.validation.validateUsername
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.shared.domain.exception.AccountPendingApprovalException
import com.teEcclesia.shared.domain.exception.EmailNotVerifiedException
import com.teEcclesia.shared.domain.exception.IncompleteProfileException
import com.teEcclesia.shared.domain.exception.PhoneNotVerifiedException
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_login
import teecclesia.designsystem.generated.resources.invalid_phone_or_national_id_or_code_or_email

import com.teEcclesia.identity.domain.repository.ProfileRepository

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    appLocalizer: AppLocalizer,
) : BaseViewModel<LoginScreenState>(LoginScreenState()), LoginInteractionListener {

    init {
        val language = settingsRepository.getCurrentAppLanguage()
        val deviceLanguage = appLocalizer.getDeviceLanguageIso()
        val enOrAr = if (deviceLanguage.startsWith(AppLanguage.ARABIC.iso)) AppLanguage.ARABIC else AppLanguage.ENGLISH
        updateState {
            copy(
                selectedLanguage = when (language) {
                    AppLanguage.DEFAULT -> enOrAr
                    else -> language
                }
            )
        }
    }

    override fun onLoginClicked() {
        validateFields()
        if (state.value.usernameError != null || state.value.passwordError != null) return

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                authenticationRepository.login(
                    LoginRequest(
                        identifier = state.value.username,
                        password = state.value.password
                    )
                )
                profileRepository.getRegistrationProfile()
            },
            onSuccess = { },
            onError = { error ->
                when (error) {
                    is IncompleteProfileException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken)
                            }
                        }
                        navigate(SignUpRoute())
                    }
                    is PhoneNotVerifiedException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken)
                            }
                        }
                        navigate(SignUpRoute())
                    }
                    is AccountPendingApprovalException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken)
                            }
                        }
                        resetTo(PendingApprovalRoute)
                    }
                    is EmailNotVerifiedException -> {
                        showSnackBar(
                            title = UiText.StringRes(Res.string.failed_to_login),
                            message = getLocalizedErrorMessage(error),
                            isSuccess = false,
                        )
                    }
                    else -> {
                        showSnackBar(
                            title = UiText.StringRes(Res.string.failed_to_login),
                            message = getLocalizedErrorMessage(error),
                            isSuccess = false,
                        )
                    }
                }
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
        when (validateUsername(state.value.username)) {
            true -> updateState { copy(usernameError = null) }
            else -> updateState { copy(usernameError = UiText.StringRes(Res.string.invalid_phone_or_national_id_or_code_or_email)) }
        }
    }

    private fun validatePassword() {
        val error = getPasswordValidationError(state.value.password)
        updateState { copy(passwordError = error?.toUiText()) }
    }

    override fun onSignUpClicked() {
        navigate(SignUpRoute())
    }

    override fun onForgotPasswordClicked() {
        navigate(ForgotPasswordRoute(key = state.value.username))
    }

    override fun onUsernameChange(newUsername: String) {
        updateState { copy(username = newUsername.trim(), usernameError = null) }
    }

    override fun onPasswordChange(newPassword: String) {
        updateState { copy(password = newPassword.trim(), passwordError = null) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onLanguageSelected(language: AppLanguage) {
        updateState { copy(selectedLanguage = language) }
        tryToCall(
            block = { settingsRepository.applyLanguage(language) },
            onSuccess = {},
            onError = {}
        )
    }

    override fun onThemeSelected(theme: AppTheme) {
        tryToCall(
            block = { settingsRepository.applyAppTheme(theme) },
            onSuccess = {},
            onError = {}
        )
    }

    override fun onContinueClicked() {
        updateState { copy(isOnboarding = false) }
    }

    override fun onBackPressed() {
        if (state.value.isOnboarding) {
            popBackStack()
        } else {
            updateState { copy(isOnboarding = true) }
        }
    }
}
