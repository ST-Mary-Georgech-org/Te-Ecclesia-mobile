package com.teEcclesia.identity.presentation.screen.resetPassword.forgotPassword

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyEmailResetPasswordRoute
import com.teEcclesia.identity.api.VerifyPhoneResetPasswordRoute
import com.teEcclesia.identity.domain.model.VerificationMethod
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.shared.domain.utils.validation.isValidEgyptianNationalId
import com.teEcclesia.shared.domain.utils.validation.isValidFinalEmail
import com.teEcclesia.shared.domain.utils.validation.validatePhone
import com.teEcclesia.shared.domain.exception.DuplicatePhoneException
import com.teEcclesia.shared.domain.exception.UsernameOrPhoneNumberAlreadyExistsException
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_duplicate_phone_forgot_password
import teecclesia.designsystem.generated.resources.failed_to_send_otp
import teecclesia.designsystem.generated.resources.invalid_phone_or_national_id_or_code_or_email

class ForgotPasswordViewModel(
    initialKey: String = "",
    private val resetPasswordRepository: ResetPasswordRepository
) : BaseViewModel<ForgotPasswordUiState>(ForgotPasswordUiState(identifier = initialKey)), ForgotPasswordInteractionListener {

    override fun onIdentifierChange(value: String) {
        updateState { copy(identifier = value.trim(), identifierError = null) }
    }

    override fun onNextClicked() {
        val input = state.value.identifier.trim()
        val isPhone = validatePhone(input)
        val isEmail = isValidFinalEmail(input)
        val isNationalId = input.matches(Regex("""\d{14}""")) || isValidEgyptianNationalId(input)

        if (!isPhone && !isEmail && !isNationalId) {
            updateState {
                copy(identifierError = UiText.StringRes(Res.string.invalid_phone_or_national_id_or_code_or_email))
            }
            return
        }

        val method = if (isPhone || isNationalId) VerificationMethod.PHONE else VerificationMethod.EMAIL

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                resetPasswordRepository.requestOTP(key = input, method = method)
            },
            onSuccess = { response ->
                if (isPhone || isNationalId) {
                    navigate(
                        VerifyPhoneResetPasswordRoute(
                            phone = input,
                            token = response?.token ?: "",
                            link = response?.link ?: ""
                        )
                    )
                } else {
                    navigate(VerifyEmailResetPasswordRoute(email = input))
                }
            },
            onError = { error ->
                val message = when (error) {
                    is UsernameOrPhoneNumberAlreadyExistsException, is DuplicatePhoneException -> {
                        UiText.StringRes(Res.string.error_duplicate_phone_forgot_password)
                    }
                    else -> getLocalizedErrorMessage(error)
                }
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_send_otp),
                    message = message,
                    isSuccess = false
                )
            },
            onEnd = {
                updateState { copy(actionButtonState = AppButtonState.Enabled) }
            }
        )
    }

    override fun onBackToLoginClicked() {
        popBackStack()
    }

    override fun onSignUpClicked() {
        navigate(SignUpRoute())
    }
}
