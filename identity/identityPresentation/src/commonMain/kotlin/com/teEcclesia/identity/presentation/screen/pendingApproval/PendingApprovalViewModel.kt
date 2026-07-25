package com.teEcclesia.identity.presentation.screen.pendingApproval

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

class PendingApprovalViewModel(
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel<PendingApprovalScreenState>(PendingApprovalScreenState()), PendingApprovalInteractionListener {

    override fun onClickLogout() {
        tryToCall(
            onStart = { updateState { copy(actionButtonState = AppButtonState.Loading) } },
            block = { authenticationRepository.clearAuthTokens() },
            onSuccess = {
                resetTo(LoginRoute)
            },
            onError = {
                resetTo(LoginRoute)
            },
            onEnd = { updateState { copy(actionButtonState = AppButtonState.Enabled) } }
        )
    }

    override fun onClickEditRequest() {
        resetTo(listOf(LoginRoute, SignUpRoute(isEditMode = true)))
    }
}
