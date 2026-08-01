package com.teEcclesia.identity.presentation.screen.resetPassword.verifyPhone

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.CreateNewPasswordRoute
import com.teEcclesia.identity.domain.model.VerificationMethod
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_verify_code

class VerifyPhoneResetPasswordViewModel(
    phone: String,
    token: String,
    link: String,
    private val resetPasswordRepository: ResetPasswordRepository
) : BaseViewModel<VerifyPhoneResetPasswordUiState>(
    VerifyPhoneResetPasswordUiState(
        phone = phone,
        token = token,
        link = link
    )
), VerifyPhoneResetPasswordInteractionListener {

    override fun onVerifyClicked() {
    }

    override fun onNextClicked() {
        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                resetPasswordRepository.verifyOTPCode(
                    key = state.value.phone,
                    otp = state.value.token,
                    method = VerificationMethod.PHONE
                )
            },
            onSuccess = {
                navigate(
                    CreateNewPasswordRoute(
                        key = state.value.phone,
                        otp = state.value.token,
                        isPhone = true
                    )
                )
            },
            onError = { error ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_verify_code),
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
