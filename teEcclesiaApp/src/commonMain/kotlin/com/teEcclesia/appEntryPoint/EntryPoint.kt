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
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyPhoneRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.navigation.AppBottomNavigationBar
import com.teEcclesia.navigation.NavigationRoot
import com.teEcclesia.util.buildNavigationSerializerConfig
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EntryPoint(
    viewModel: MainEntryViewModel = koinViewModel(),
    authorizationService: AuthorizationService = koinInject(),
    effector: Effector = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val accessToken by authorizationService.observeAccessToken().collectAsStateWithLifecycle()

    val navigationSerializerConfig = buildNavigationSerializerConfig()
    val backStack = rememberNavBackStack(navigationSerializerConfig, LoginRoute)
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
        }
    }

    val showBottomNavigation = currentRoute is HomeRoute
            || currentRoute is ProfileRoute

    LaunchedEffect(accessToken) {
        val targetRoute = if (accessToken.isBlank()) LoginRoute else HomeRoute
        val isUnauthRoute = currentRoute == LoginRoute
                || currentRoute == SignUpRoute
                || currentRoute is VerifyPhoneRoute

        if (targetRoute == HomeRoute) {
            if (isUnauthRoute) {
                effector.resetTo(targetRoute, true)
            }
        } else {
            if (currentRoute != targetRoute) {
                effector.resetTo(targetRoute, true)
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
