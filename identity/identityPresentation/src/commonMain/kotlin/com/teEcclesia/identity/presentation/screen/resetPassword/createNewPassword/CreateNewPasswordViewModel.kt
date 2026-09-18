package com.teEcclesia.identity.presentation.screen.resetPassword.createNewPassword

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.domain.model.VerificationMethod
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toUiText
import com.teEcclesia.shared.domain.utils.validation.getPasswordValidationError
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_reset_password
import teecclesia.designsystem.generated.resources.password_reset_and_account_reactivated_successfully
import teecclesia.designsystem.generated.resources.password_reset_successfully

class CreateNewPasswordViewModel(
    key: String,
    otp: String,
    isPhone: Boolean,
    isDeletedAccount: Boolean,
    private val resetPasswordRepository: ResetPasswordRepository
) : BaseViewModel<CreateNewPasswordUiState>(
    CreateNewPasswordUiState(
        key = key,
        otp = otp,
        method = if (isPhone) VerificationMethod.PHONE else VerificationMethod.EMAIL,
        isDeletedAccount = isDeletedAccount
    )
), CreateNewPasswordInteractionListener {

    override fun onPasswordChange(value: String) {
        val error = getPasswordValidationError(value)
        updateState { copy(password = value, passwordError = error?.toUiText()) }
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onLoginClicked() {
        val error = getPasswordValidationError(state.value.password)
        if (error != null) {
            updateState { copy(passwordError = error.toUiText()) }
            return
        }

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                resetPasswordRepository.resetPassword(
                    key = state.value.key,
                    otp = state.value.otp,
                    newPassword = state.value.password,
                    method = state.value.method
                )
            },
            onSuccess = {
                val successMessage = if (state.value.isDeletedAccount) {
                    Res.string.password_reset_and_account_reactivated_successfully
                } else {
                    Res.string.password_reset_successfully
                }
                showSnackBar(
                    title = UiText.StringRes(successMessage),
                    isSuccess = true
                )
                resetTo(LoginRoute)
            },
            onError = { error ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_reset_password),
                    message = getLocalizedErrorMessage(error),
                    isSuccess = false
                )
            },
            onEnd = {
                updateState { copy(actionButtonState = AppButtonState.Enabled) }
            }
        )
    }
}
