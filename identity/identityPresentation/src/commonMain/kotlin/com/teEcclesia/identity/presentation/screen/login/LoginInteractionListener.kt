package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme

interface LoginInteractionListener {
    fun onLoginClicked()
    fun onSignUpClicked()
    fun onForgotPasswordClicked()
    fun onUsernameChange(newUsername: String)
    fun onPasswordChange(newPassword: String)
    fun onTogglePasswordVisibility()
    fun onLanguageSelected(language: AppLanguage)
    fun onThemeSelected(theme: AppTheme)
    fun onContinueClicked()
    fun onEnableNotificationsClicked()
    fun onBackPressed()
}

