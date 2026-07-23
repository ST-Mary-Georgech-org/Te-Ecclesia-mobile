package com.teEcclesia.identity.presentation.screen.pendingApproval

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

class PendingApprovalViewModel(
    private val authenticationRepository: AuthenticationRepository
) : BaseViewModel<PendingApprovalScreenState>(PendingApprovalScreenState()), PendingApprovalInteractionListener {

    override fun onClickLogout() {
        tryToCall(
            block = { authenticationRepository.clearAuthTokens() },
            onSuccess = {
                resetTo(LoginRoute)
            },
            onError = {
                resetTo(LoginRoute)
            }
        )
    }

    override fun onClickEditRequest() {
        resetTo(SignUpRoute(isEditMode = true))
    }
}
