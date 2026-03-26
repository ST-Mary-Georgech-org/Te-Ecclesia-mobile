package com.teEcclesia.identity.presentation.screen.verifyPhone

import com.teEcclesia.designsystem.utils.UiText

data class VerifyPhoneUiState(
    val isLoading: Boolean = false,
    val phone: String = "",
    val isForgetPasswordFlow: Boolean = true,
    val otp: String = "",
    val otpError: UiText? = null,
    val timeRemaining: Int = 30,
    val canResend: Boolean = false,
)

