package com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.VerificationMethod

data class CreateNewPasswordUiState(
    val key: String = "",
    val otp: String = "",
    val method: VerificationMethod = VerificationMethod.EMAIL,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val actionButtonState: AppButtonState = AppButtonState.Enabled
)
