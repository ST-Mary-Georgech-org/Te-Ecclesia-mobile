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
import com.teEcclesia.identity.api.VerifyPhoneRoute
import com.teEcclesia.identity.domain.model.AuthState
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

class MainEntryViewModel(
    private val authorizationService: AuthorizationService,
    private val profileRepository: ProfileRepository,
) : BaseViewModel<MainEntryState>(MainEntryState()),
    MainEntryInteractionListener {

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

    private var lastHandledAuthState: AuthState? = null

    private var job: Job? = null

    fun handleAuthState(authState: AuthState, currentRoute: NavKey?) {
        job?.cancel()
        job = launch {
            val isStateChanged = lastHandledAuthState != authState
            lastHandledAuthState = authState

            val isUnauthRoute = currentRoute == LoginRoute
                    || currentRoute is SignUpRoute
                    || currentRoute is VerifyPhoneRoute

            when (authState) {

                AuthState.AUTHENTICATED -> {
                    val previousStatus = authorizationService.getUserStatus()
                    navigateByState(previousStatus, currentRoute, isUnauthRoute)

                    if (isStateChanged || isUnauthRoute) {
                        tryToCall(
                            block = {
                                profileRepository.getRegistrationProfile()
                            },
                            onSuccess = { profile ->
                                authorizationService.saveUserStatus(profile.status)
                                if (profile.status != previousStatus) {
                                    navigateByState(profile.status, currentRoute, isUnauthRoute)
                                }
                            },
                            onError = {
                                // Failures silently handled without UI thread locks
                            }
                        )
                    }
                }

                AuthState.REGISTRATION_PENDING -> {
                    if (isStateChanged) {
                        val userStatus = authorizationService.getUserStatus()
                        if (userStatus == UserStatus.PENDING_APPROVAL) {
                            if (currentRoute !is PendingApprovalRoute) {
                                resetTo(PendingApprovalRoute, true)
                            }
                        } else {
                            if (isUnauthRoute) {
                                resetTo(listOf(LoginRoute, SignUpRoute()), true)
                            }
                        }
                    }
                }

                AuthState.UNAUTHENTICATED -> {
                    if (!isUnauthRoute) {
                        resetTo(LoginRoute, true)
                    }
                }
            }
        }
    }

    private suspend fun navigateByState(
        status: UserStatus?,
        currentRoute: NavKey?,
        isUnauthRoute: Boolean
    ) {
        when (status) {
            UserStatus.PENDING_APPROVAL -> {
                if (currentRoute !is PendingApprovalRoute) {
                    resetTo(PendingApprovalRoute, true)
                }
            }

            UserStatus.APPROVED -> {
                if (isUnauthRoute || currentRoute is PendingApprovalRoute) {
                    resetTo(ProfileRoute, true)
                }
            }

            UserStatus.REJECTED, UserStatus.BANNED, null -> {
                authorizationService.clearAuthTokens()
                resetTo(LoginRoute, true)
            }

            else -> {
                if (isUnauthRoute) {
                    resetTo(ProfileRoute, true)
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

    fun getStaticInitialRoute(authState: AuthState): NavKey {
        return when (authState) {
            AuthState.AUTHENTICATED -> {
                val status = authorizationService.getUserStatus()
                when (status) {
                    UserStatus.PENDING_APPROVAL -> PendingApprovalRoute
                    UserStatus.APPROVED -> ProfileRoute
                    UserStatus.REJECTED, UserStatus.BANNED, null -> LoginRoute
                    else -> ProfileRoute
                }
            }

            AuthState.REGISTRATION_PENDING -> {
                val status = authorizationService.getUserStatus()
                if (status == UserStatus.PENDING_APPROVAL) {
                    PendingApprovalRoute
                } else {
                    SignUpRoute()
                }
            }

            AuthState.UNAUTHENTICATED -> LoginRoute
        }
    }
}
