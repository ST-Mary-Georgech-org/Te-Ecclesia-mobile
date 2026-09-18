package com.teEcclesia.manager

import com.teEcclesia.designsystem.navigation.SnackBarManager
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.shared.domain.manager.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_unauthorized
import teecclesia.designsystem.generated.resources.error_user_not_verified

class SessionManagerImpl(
    private val authenticationRepository: AuthenticationRepository,
    private val effector: Effector,
    private val snackBarManager: SnackBarManager
) : SessionManager {

    override suspend fun onSessionExpired(message: String?) {
        withContext(Dispatchers.IO) {
            authenticationRepository.clearAuthTokens()
            effector.resetTo(LoginRoute, forceNavigate = true)
            snackBarManager.showSnackBar(
                title = UiText.StringRes(Res.string.error_unauthorized),
                message = message?.ifBlank { null }?.let { UiText.DynamicString(it) },
                isSuccess = false
            )
        }
    }

    override suspend fun onUserBlocked(message: String?) {
        withContext(Dispatchers.IO) {
            authenticationRepository.clearAuthTokens()
            effector.resetTo(LoginRoute, forceNavigate = true)
            snackBarManager.showSnackBar(
                title = UiText.StringRes(Res.string.error_user_not_verified),
                message = message?.ifBlank { null }?.let { UiText.DynamicString(it) },
                isSuccess = false
            )
        }
    }
}
