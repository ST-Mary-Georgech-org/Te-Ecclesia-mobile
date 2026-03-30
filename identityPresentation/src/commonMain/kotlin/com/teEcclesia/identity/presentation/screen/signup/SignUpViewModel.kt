package com.teEcclesia.identity.presentation.screen.signup

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.domain.useCase.validation.auth.ValidationUseCase
import com.teEcclesia.identity.presentation.navigation.VerifyPhoneRoute
import com.teEcclesia.identity.presentation.shared.BaseViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.please_enter_your_full_name
import teecclesia.designsystem.generated.resources.invalid_password
import teecclesia.designsystem.generated.resources.invalid_phone_number
import teecclesia.designsystem.generated.resources.invalid_username
import teecclesia.designsystem.generated.resources.unknown_error

class SignUpViewModel(
    private val registerRepository: RegisterRepository,
    private val validationUseCase: ValidationUseCase,
) : BaseViewModel<SignUpUiState>(SignUpUiState()), SignUpInteractionListener {

    override fun onNameChange(newName: String) {
        updateState { copy(fullName = newName) }
        validateFullName()
    }

    override fun showDatePicker() {
        updateState { copy(showDatePicker = true) }
    }

    override fun onChangeUsername(newUsername: String) {
        updateState { copy(username = newUsername.replace(Regex("[^A-Za-z0-9_]"), "")) }
        validateUsername()
    }

    override fun onDismissDatePicker() {
        updateState { copy(showDatePicker = false) }
    }

    override fun onPhoneChange(newPhone: String) {
        updateState { copy(phone = newPhone.replace(Regex("(?!^)\\+|[^\\d+]"), "")) }
        validatePhone()
    }

    override fun onPasswordChange(newPassword: String) {
        updateState { copy(password = newPassword) }
        validatePassword()
    }

    override fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    override fun onTermsAndConditionsClicked() {
        updateState { copy(isTermsAndConditionsBottomSheetVisible = true) }
    }

    override fun onPrivacyPolicyClicked() {
        updateState { copy(isPrivacyPolicyBottomSheetVisible = true) }
    }

    override fun onTermsAndConditionsBottomSheetDismissed() {
        updateState { copy(isTermsAndConditionsBottomSheetVisible = false) }
    }

    override fun onPrivacyPolicyBottomSheetDismissed() {
        updateState { copy(isPrivacyPolicyBottomSheetVisible = false) }
    }


    override fun onSignUpClicked() {
        validateFields()

        if (state.value.fullNameError != null || state.value.userNameError != null || state.value.phoneError != null || state.value.passwordError != null) {
            return
        }

        tryToCall(
            onStart = {
                updateState { copy(actionButtonState = AppButtonState.Loading) }
            },
            block = {
                registerRepository.register(
                    RegisterRequest(
                        username = state.value.username,
                        name = state.value.fullName,
                        phone = state.value.phone,
                        password = state.value.password,
                    )
                )
            },
            onSuccess = {
                navigate(VerifyPhoneRoute(phone = state.value.phone, isForgetPasswordFlow = false))
            },
            onError = { error ->
                showSnackBar(
                    UiText.StringRes(Res.string.error_occurred),
                    message = error.message?.let { UiText.DynamicString(it) } ?: UiText.StringRes(
                        Res.string.unknown_error
                    ),
                    isSuccess = false
                )
            },
            onEnd = {
                updateState { copy(actionButtonState = AppButtonState.Enabled) }
            }
        )
    }

    override fun onLoginClicked() {
        popBackStack()
    }

    private fun validateFields() {
        validateFullName()
        validateUsername()
        validatePhone()
        validatePassword()
    }

    private fun validateFullName() {
        val isValid = validationUseCase.validateName(state.value.fullName)
        updateState {
            copy(
                fullNameError = if (isValid) {
                    null
                } else {
                    UiText.StringRes(Res.string.please_enter_your_full_name)
                }
            )
        }
    }

    private fun validateUsername() {
        val isValid = validationUseCase.validateUsername(state.value.username)
        updateState {
            copy(
                userNameError = if (isValid) {
                    null
                } else {
                    UiText.StringRes(Res.string.invalid_username)
                }
            )
        }
    }

    private fun validatePhone() {
        val isValid = validationUseCase.validatePhone(state.value.phone)
        updateState {
            copy(
                phoneError = if (isValid) {
                    null
                } else {
                    UiText.StringRes(Res.string.invalid_phone_number)
                }
            )
        }
    }

    private fun validatePassword() {
        val isValid = validationUseCase.validatePassword(state.value.password)
        updateState {
            copy(
                passwordError = if (isValid) {
                    null
                } else {
                    UiText.StringRes(Res.string.invalid_password)
                }
            )
        }
    }
}