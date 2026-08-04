package com.teEcclesia.identity.presentation.screen.resetPassword.verifyEmail

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.CreateNewPasswordRoute
import com.teEcclesia.identity.domain.model.VerificationMethod
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_send_otp
import teecclesia.designsystem.generated.resources.failed_to_verify_code
import teecclesia.designsystem.generated.resources.otp_must_be_5_digits

class VerifyEmailResetPasswordViewModel(
    email: String,
    private val resetPasswordRepository: ResetPasswordRepository
) : BaseViewModel<VerifyEmailResetPasswordUiState>(
    VerifyEmailResetPasswordUiState(email = email)
), VerifyEmailResetPasswordInteractionListener {

    override fun onOtpChange(value: String) {
        updateState { copy(otpCode = value, otpError = null) }
    }

    override fun onVerifyCodeClicked() {
        val otp = state.value.otpCode
        if (otp.length != 5) {
            updateState { copy(otpError = UiText.StringRes(Res.string.otp_must_be_5_digits)) }
            return
        }

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                resetPasswordRepository.verifyOTPCode(
                    key = state.value.email,
                    otp = otp,
                    method = VerificationMethod.EMAIL
                )
            },
            onSuccess = {
                navigate(
                    CreateNewPasswordRoute(
                        key = state.value.email,
                        otp = otp,
                        isPhone = false
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

    override fun onResendClicked() {
        tryToCall(
            block = {
                resetPasswordRepository.reSendOtp(
                    key = state.value.email,
                    method = VerificationMethod.EMAIL
                )
            },
            onSuccess = { },
            onError = { error ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_send_otp),
                    message = getLocalizedErrorMessage(error),
                    isSuccess = false
                )
            }
        )
    }
}
