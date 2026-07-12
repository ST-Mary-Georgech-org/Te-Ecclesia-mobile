package com.teEcclesia.identity.presentation.screen.signup

interface SignUpInteractionListener {
    fun onSignUpClicked()
    fun onLoginClicked()
    fun onNameChange(newName: String)
    fun showDatePicker()
    fun onChangeUsername(newUsername: String)
    fun onDismissDatePicker()
    fun onPhoneChange(newPhone: String)
    fun onPasswordChange(newPassword: String)
    fun onTogglePasswordVisibility()
    fun onTermsAndConditionsClicked()
    fun onPrivacyPolicyClicked()
    fun onTermsAndConditionsBottomSheetDismissed()
    fun onPrivacyPolicyBottomSheetDismissed()
}
