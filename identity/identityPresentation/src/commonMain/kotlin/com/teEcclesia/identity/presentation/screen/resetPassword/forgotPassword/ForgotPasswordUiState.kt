package com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText

data class ForgotPasswordUiState(
    val identifier: String = "",
    val identifierError: UiText? = null,
    val actionButtonState: AppButtonState = AppButtonState.Enabled
)
