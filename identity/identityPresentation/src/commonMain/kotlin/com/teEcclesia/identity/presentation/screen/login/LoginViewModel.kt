package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.identity.api.ForgotPasswordRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.LoginRequest
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.presentation.util.toUiText
import com.teEcclesia.shared.domain.utils.validation.getPasswordValidationError
import com.teEcclesia.shared.domain.utils.validation.validateUsername
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.shared.domain.exception.AccountPendingApprovalException
import com.teEcclesia.shared.domain.exception.EmailNotVerifiedException
import com.teEcclesia.shared.domain.exception.IncompleteProfileException
import com.teEcclesia.shared.domain.exception.PhoneNotVerifiedException
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_login
import teecclesia.designsystem.generated.resources.invalid_phone_or_national_id_or_code_or_email
import teecclesia.designsystem.generated.resources.error_kids_app_age_restriction
import teecclesia.designsystem.generated.resources.error_full_app_age_restriction
import com.teEcclesia.shared.domain.config.AppFlavorConfig
import com.teEcclesia.shared.domain.utils.validation.isValidEgyptianNationalId
import com.teEcclesia.shared.domain.utils.validation.isUnderAgeFromEgyptianNationalId

import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.shared.domain.push.NotificationPermissionHandler

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
    private val settingsRepository: SettingsRepository,
    appLocalizer: AppLocalizer,
    private val notificationPermissionHandler: NotificationPermissionHandler,
    private val appFlavorConfig: AppFlavorConfig,
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
        checkNotificationPermission()
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
            onSuccess = { profile ->
                val isChildUnder13 = if (profile.nationalId.length == 14 && isValidEgyptianNationalId(profile.nationalId)) {
                    isUnderAgeFromEgyptianNationalId(profile.nationalId, 13)
                } else {
                    null
                }

                val isAgeMismatch = when {
                    appFlavorConfig.isKidsVersion && isChildUnder13 == false -> true
                    !appFlavorConfig.isKidsVersion && isChildUnder13 == true -> true
                    else -> false
                }

                if (isAgeMismatch) {
                    launch { authenticationRepository.logout() }
                    val errorMsg = if (appFlavorConfig.isKidsVersion) {
                        UiText.StringRes(Res.string.error_kids_app_age_restriction)
                    } else {
                        UiText.StringRes(Res.string.error_full_app_age_restriction)
                    }
                    updateState {
                        copy(
                            usernameError = errorMsg,
                            password = "",
                            passwordError = null
                        )
                    }
                } else {
                    when (profile.status) {
                        UserStatus.PENDING_APPROVAL -> resetTo(PendingApprovalRoute, forceNavigate = true)
                        UserStatus.APPROVED -> resetTo(ProfileRoute, forceNavigate = true)
                        UserStatus.PROFILE_INCOMPLETE,
                        UserStatus.UNVERIFIED -> resetTo(SignUpRoute(), forceNavigate = true)
                        UserStatus.REJECTED,
                        UserStatus.BANNED -> {
                            launch { authenticationRepository.clearAuthTokens() }
                        }
                    }
                }
            },
            onError = { error ->
                when (error) {
                    is IncompleteProfileException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken, syncDeviceToken = false)
                            }
                        }
                        navigate(SignUpRoute())
                    }
                    is PhoneNotVerifiedException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken, syncDeviceToken = false)
                            }
                        }
                        navigate(SignUpRoute())
                    }
                    is AccountPendingApprovalException -> {
                        val token = error.token
                        val refreshToken = error.refreshToken ?: ""
                        if (!token.isNullOrBlank()) {
                            launch {
                                authenticationRepository.saveRegistrationToken(token, refreshToken, syncDeviceToken = false)
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

    fun checkNotificationPermission() {
        notificationPermissionHandler.checkPermission { isGranted ->
            updateState { copy(isNotificationPermissionGranted = isGranted) }
        }
    }

    override fun onEnableNotificationsClicked() {
        notificationPermissionHandler.openNotificationSettings()
    }
}

