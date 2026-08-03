package com.teEcclesia.identity.presentation.screen.pendingApproval

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred

class PendingApprovalViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val profileRepository: ProfileRepository,
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

    override fun onRefresh() {
        tryToCall(
            onStart = { updateState { copy(isRefreshing = true) } },
            block = { profileRepository.getRegistrationProfile() },
            onSuccess = { },
            onEnd = { updateState { copy(isRefreshing = false) } },
            onError = {
                showSnackBar(
                    title = UiText.StringRes(Res.string.error_occurred),
                    message = getLocalizedErrorMessage(it),
                    isSuccess = false
                )
            }
        )
    }
}
