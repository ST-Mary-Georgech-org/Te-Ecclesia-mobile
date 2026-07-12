package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText

data class LoginScreenState(
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val username: String = "",
    val usernameError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,
)
