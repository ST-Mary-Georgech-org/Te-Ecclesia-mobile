package com.teEcclesia.identity.presentation.screen.signup

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText

data class SignUpUiState(
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val username: String = "",
    val userNameError: UiText? = null,
    val fullName: String = "",
    val fullNameError: UiText? = null,
    val phone: String = "",
    val phoneError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = true,
    val isTermsAndConditionsBottomSheetVisible: Boolean = false,
    val isPrivacyPolicyBottomSheetVisible: Boolean = false,
    val showDatePicker: Boolean = false
)
