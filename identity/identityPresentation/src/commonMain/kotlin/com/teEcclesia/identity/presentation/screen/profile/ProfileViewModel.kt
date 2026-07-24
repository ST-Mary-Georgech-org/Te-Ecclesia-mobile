package com.teEcclesia.identity.presentation.screen.profile

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
) : BaseViewModel<ProfileScreenState>(ProfileScreenState()) {

    fun onClickLogout() {
        tryToCall(
            onStart = { updateState { copy(actionButtonState = AppButtonState.Loading) } },
            block = { authenticationRepository.logout() },
            onSuccess = {  },
            onError = {  },
            onEnd = { updateState { copy(actionButtonState = AppButtonState.Enabled) } }
        )
        resetTo(LoginRoute)
    }
}
