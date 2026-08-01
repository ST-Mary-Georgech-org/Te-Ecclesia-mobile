package com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail

interface VerifyEmailResetPasswordInteractionListener {
    fun onOtpChange(value: String)
    fun onVerifyCodeClicked()
    fun onResendClicked()
}
