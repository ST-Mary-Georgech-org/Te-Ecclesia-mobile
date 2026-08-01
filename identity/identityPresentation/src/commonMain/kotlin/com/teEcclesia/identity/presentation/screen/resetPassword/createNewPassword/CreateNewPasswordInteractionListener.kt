package com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword

interface CreateNewPasswordInteractionListener {
    fun onPasswordChange(value: String)
    fun onTogglePasswordVisibility()
    fun onLoginClicked()
}
