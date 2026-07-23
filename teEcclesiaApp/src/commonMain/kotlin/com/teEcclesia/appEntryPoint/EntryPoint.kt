package com.teEcclesia.appEntryPoint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.teEcclesia.designsystem.components.snackbar.AnimatedSnackBar
import com.teEcclesia.designsystem.navigation.effector.Effect
import com.teEcclesia.designsystem.navigation.effector.EffectHandler
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.SplashRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyPhoneRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.navigation.AppBottomNavigationBar
import com.teEcclesia.navigation.NavigationRoot
import com.teEcclesia.util.buildNavigationSerializerConfig
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

import kotlinx.coroutines.launch
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.domain.model.AuthState
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.teEcclesia.identity.domain.model.UserStatus

@Composable
fun EntryPoint(
    viewModel: MainEntryViewModel = koinViewModel(),
    authorizationService: AuthorizationService = koinInject(),
    profileRepository: ProfileRepository = koinInject(),
    effector: Effector = koinInject(),
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by authorizationService.observeAuthState().collectAsStateWithLifecycle()

    val navigationSerializerConfig = buildNavigationSerializerConfig()
    val backStack = rememberNavBackStack(navigationSerializerConfig, SplashRoute)
    val currentRoute = backStack.lastOrNull()

    EffectHandler(effector.effect) { effect ->
        when (effect) {
            is Effect.Navigate -> {
                if (effect.route != currentRoute) backStack.add(effect.route)
            }

            is Effect.PopBackStack -> {
                backStack.removeLastOrNull()
            }

            is Effect.ResetTo -> {
                backStack.clear()
                backStack.add(effect.route)
            }

            is Effect.ResetToMultiple -> {
                backStack.clear()
                backStack.addAll(effect.routes)
            }
        }
    }

    val showBottomNavigation = currentRoute is HomeRoute
            || currentRoute is ProfileRoute

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.AUTHENTICATED -> {
                launch {
                    runCatching {
                        val previousStatus = authorizationService.getUserStatus()
                        val profile = profileRepository.getRegistrationProfile()
                        authorizationService.saveUserRole(profile.role)
                        authorizationService.saveUserStatus(profile.status)

                        if (profile.status == UserStatus.PENDING_APPROVAL) {
                            if (currentRoute !is PendingApprovalRoute) {
                                effector.resetTo(PendingApprovalRoute, true)
                            }
                        } else if (previousStatus == UserStatus.PENDING_APPROVAL && profile.status == UserStatus.APPROVED) {
                            effector.resetTo(HomeRoute, true)
                        } else if (profile.status == UserStatus.REJECTED || profile.status == UserStatus.SUSPENDED) {
                            effector.resetTo(LoginRoute, true)
                        }
                    }
                }
                val isUnauthRoute = currentRoute == LoginRoute
                        || currentRoute is SignUpRoute
                        || currentRoute is VerifyPhoneRoute
                        || currentRoute is SplashRoute
                if (isUnauthRoute) {
                    effector.resetTo(HomeRoute, true)
                }
            }
            AuthState.REGISTRATION_PENDING -> {
                val userStatus = authorizationService.getUserStatus()
                if (userStatus == UserStatus.PENDING_APPROVAL) {
                    if (currentRoute !is PendingApprovalRoute) {
                        effector.resetTo(PendingApprovalRoute, true)
                    }
                } else {
                    val isUnauthRoute = currentRoute == LoginRoute
                            || currentRoute is SplashRoute
                    if (isUnauthRoute) {
                        effector.resetTo(listOf(LoginRoute, SignUpRoute()), true)
                    }
                }
            }
            AuthState.UNAUTHENTICATED -> {
                val isAuthRoute = currentRoute is HomeRoute || currentRoute is ProfileRoute
                if (isAuthRoute || currentRoute is SplashRoute) {
                    effector.resetTo(LoginRoute, true)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AnimatedSnackBar(
            isVisible = state.isSnackBarVisible,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1000f)
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 12.dp, end = 16.dp),
            onDismiss = viewModel::hideSnackBar,
            data = state.snackBarData
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationRoot(backStack)

            AppBottomNavigationBar(
                showBottomNavigation = showBottomNavigation,
                activeRoute = currentRoute,
                interactionListener = viewModel
            )
        }
    }
}
