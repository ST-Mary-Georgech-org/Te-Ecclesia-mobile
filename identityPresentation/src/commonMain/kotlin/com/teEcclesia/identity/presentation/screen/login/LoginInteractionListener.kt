package com.teEcclesia.identity.presentation.screen.login

interface LoginInteractionListener {
    fun onLoginClicked()
    fun onSignUpClicked()
    fun onForgotPasswordClicked()
    fun onUsernameChange(newUsername: String)
    fun onPasswordChange(newPassword: String)
    fun onTogglePasswordVisibility()
}
