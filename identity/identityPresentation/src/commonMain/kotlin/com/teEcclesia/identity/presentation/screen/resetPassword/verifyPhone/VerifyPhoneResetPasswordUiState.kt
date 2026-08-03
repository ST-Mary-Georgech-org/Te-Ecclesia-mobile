package com.teEcclesia.identity.presentation.screen.resetPassword.verifyPhone

import com.teEcclesia.designsystem.components.button.AppButtonState

data class VerifyPhoneResetPasswordUiState(
    val phone: String = "",
    val token: String = "",
    val link: String = "",
    val nextButtonState: AppButtonState = AppButtonState.Enabled
)
