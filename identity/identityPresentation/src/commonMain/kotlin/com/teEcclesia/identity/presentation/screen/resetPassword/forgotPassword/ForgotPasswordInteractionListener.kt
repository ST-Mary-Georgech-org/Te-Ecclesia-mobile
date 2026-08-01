package com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword

interface ForgotPasswordInteractionListener {
    fun onIdentifierChange(value: String)
    fun onNextClicked()
    fun onBackToLoginClicked()
    fun onSignUpClicked()
}
