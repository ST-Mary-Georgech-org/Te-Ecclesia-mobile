package com.teEcclesia.identity.presentation.screen.profile

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel<Unit>(Unit) {

    fun onClickLogout() {
        tryToCall(
            block = { authenticationRepository.logout() },
            onSuccess = {
                resetTo(LoginRoute)
            },
            onError = {
                resetTo(LoginRoute)
            }
        )
    }
}
