package com.teEcclesia.identity.presentation.screen.verifyPhone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.domain.useCase.validation.auth.ValidationUseCase
import com.teEcclesia.identity.presentation.navigation.VerifyPhoneRoute
import com.teEcclesia.identity.presentation.shared.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.invalid_otp_please_try_again
import teecclesia.designsystem.generated.resources.otp_must_be_4_digits
import teecclesia.designsystem.generated.resources.unknown_error

class VerifyPhoneViewModel(
    private val registerRepository: RegisterRepository,
    private val resetPasswordRepository: ResetPasswordRepository,
    private val validationUseCase: ValidationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<VerifyPhoneUiState>(VerifyPhoneUiState()), VerifyPhoneInteractionListener {

    private val navPhone = savedStateHandle.toRoute<VerifyPhoneRoute>().phone
    private val navIsForgetPasswordFlow =
        savedStateHandle.toRoute<VerifyPhoneRoute>().isForgetPasswordFlow
    private var timerJob: Job? = null

    init {
        updateState {
            copy(
                phone = navPhone,
                isForgetPasswordFlow = navIsForgetPasswordFlow,
            )
        }
        startTimer()
    }

    override fun onOtpChange(newOtp: String) {
        val onlyDigits = newOtp.filter { it.isDigit() }.take(4)
        updateState { copy(otp = onlyDigits) }

        if (onlyDigits.length == 4) {
            onVerify()
        }
    }

    private fun onVerify() {
        validateOtp()
        if (state.value.otpError != null) return

        tryToCall(
            onStart = {
                updateState { copy(isLoading = true) }
            },
            block = {
                if (state.value.isForgetPasswordFlow) {
                    resetPasswordRepository.verifyOTPCode(
                        phone = state.value.phone,
                        otp = state.value.otp,
                    )
                } else {
                    registerRepository.verifyOTPCode(
                        phone = state.value.phone,
                        otp = state.value.otp,
                    )
                }
            },
            onSuccess = {
//                if (state.value.isForgetPasswordFlow) {
//                    navigate(CreateNewPasswordRoute)
//                }
            },
            onError = {
                updateState {
                    copy(
                        otpError = UiText.StringRes(Res.string.invalid_otp_please_try_again),
                    )
                }
            },
            onEnd = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    override fun onResendClicked() {
        tryToCall(
            block = {
                if (state.value.isForgetPasswordFlow) {
                    resetPasswordRepository.reSendOtp(state.value.phone)
                } else {
                    registerRepository.reSendOTP(state.value.phone)
                }
            },
            onSuccess = {
                updateState {
                    it.copy(
                        timeRemaining = 50,
                        canResend = false,
                        otpError = null,
                    )
                }
                startTimer()
            },
            onError = { error ->
                showSnackBar(
                    UiText.StringRes(Res.string.error_occurred),
                    message = error.message?.let { UiText.DynamicString(it) } ?: UiText.StringRes(
                        Res.string.unknown_error
                    ),
                    isSuccess = false
                )
            }
        )
    }

    private fun validateOtp() {
        val isValid = validationUseCase.validateOtp(state.value.otp)
        updateState {
            copy(
                otpError = if (isValid) {
                    null
                } else {
                    UiText.StringRes(Res.string.otp_must_be_4_digits)
                }
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (state.value.timeRemaining > 0) {
                delay(1000)
                updateState {
                    it.copy(
                        timeRemaining = it.timeRemaining - 1,
                        canResend = it.timeRemaining - 1 <= 0
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}


