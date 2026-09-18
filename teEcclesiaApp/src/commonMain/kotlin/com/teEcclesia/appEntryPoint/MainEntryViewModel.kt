package com.teEcclesia.appEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation3.runtime.NavKey
import com.teEcclesia.designsystem.components.snackbar.SnackBarData
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.asStringSuspend
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.shared.domain.push.PushTokenProvider
import kotlinx.coroutines.flow.collectLatest

class MainEntryViewModel(
    private val authorizationService: AuthorizationService,
    private val profileRepository: ProfileRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val pushTokenProvider: PushTokenProvider,
) : BaseViewModel<MainEntryState>(MainEntryState()),
    MainEntryInteractionListener {

    val startDestination: NavKey = getInitialRoute()

    private fun getInitialRoute(): NavKey {
        val accessToken = authorizationService.getAccessToken()
        if (accessToken.isBlank()) return LoginRoute
        return when (authorizationService.getUserStatus()) {
            UserStatus.PENDING_APPROVAL -> PendingApprovalRoute
            UserStatus.APPROVED -> ProfileRoute
            UserStatus.PROFILE_INCOMPLETE, UserStatus.UNVERIFIED -> SignUpRoute()
            UserStatus.REJECTED, UserStatus.BANNED -> LoginRoute
            null -> ProfileRoute
        }
    }

    init {
        launch {
            snackBarManager.snackBarEvent.collectLatest { event ->
                val resolvedTitle = event.title.asStringSuspend()
                val resolvedMessage = event.message?.asStringSuspend()
                updateState {
                    it.copy(
                        isSnackBarVisible = true,
                        snackBarData = SnackBarData(
                            title = resolvedTitle,
                            message = resolvedMessage,
                            isSuccess = event.isSuccess,
                            customLeadingIcon = event.customLeadingIcon,
                            duration = event.duration,
                            iconTint = event.iconTint
                        )
                    )
                }
            }
        }
    }

    fun syncPushTokenIfLoggedIn() {
        if (authorizationService.getAccessToken().isNotBlank()) {
            launch {
                try {
                    val token = pushTokenProvider.getToken()
                    if (!token.isNullOrBlank()) {
                        authenticationRepository.updateDeviceToken(token)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBottomNavigationChanged(isShowed: Boolean) {
        updateState { it.copy(showBottomNavigation = isShowed) }
    }

    override fun showSnackBar(
        title: String,
        message: String?,
        isSuccess: Boolean,
        customLeadingIcon: Painter?,
        duration: Long?,
        iconTint: Color
    ) {
        updateState {
            it.copy(
                isSnackBarVisible = true,
                snackBarData = SnackBarData(
                    title = title,
                    message = message,
                    isSuccess = isSuccess,
                    customLeadingIcon = customLeadingIcon,
                    duration = duration,
                    iconTint = iconTint
                )
            )
        }
    }

    override fun hideSnackBar() {
        updateState { it.copy(isSnackBarVisible = false) }
    }

    override fun resetToRoute(route: NavKey, forceNavigate: Boolean) {
        resetTo(route, forceNavigate)
    }

    override fun navigateToRoute(route: NavKey, forceNavigate: Boolean) {
        navigate(route, forceNavigate)
    }
}
