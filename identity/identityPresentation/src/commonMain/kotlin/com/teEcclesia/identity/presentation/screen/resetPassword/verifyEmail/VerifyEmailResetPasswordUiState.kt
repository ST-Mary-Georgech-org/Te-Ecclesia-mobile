package com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText

data class VerifyEmailResetPasswordUiState(
    val email: String = "",
    val otpCode: String = "",
    val otpError: UiText? = null,
    val actionButtonState: AppButtonState = AppButtonState.Enabled
)
