package com.teEcclesia.identity.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.home.api.HomeFeatureApi
import com.teEcclesia.identity.presentation.screen.login.LoginScreen
import com.teEcclesia.identity.presentation.screen.verifyPhone.VerifyPhoneScreen
import com.teEcclesia.identity.presentation.screen.signup.SignUpScreen
import org.koin.compose.koinInject
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.profile

@Composable
fun IdentityNavGraph(
    navController: NavHostController,
    startDestination: BaseRoute,
    updateBottomNavigationVisibility: (Boolean) -> Unit,
    homeFeatureApi: HomeFeatureApi = koinInject(),
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
    ) {
        composable<LoginRoute> { LoginScreen() }
        composable<SignUpRoute> { SignUpScreen() }
        composable<VerifyPhoneRoute> { VerifyPhoneScreen() }
        composable<ProfileRoute> {
            Box(
                Modifier.fillMaxSize().background(Color.Blue),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column {
                    Text(Res.string.profile.asString(), Theme.typography.label.medium.medium)
                }
            }
        }
        composable<HomeRoute> {
            homeFeatureApi.TabEntry(
                updateBottomNavigationVisibility = updateBottomNavigationVisibility
            )
        }
    }
}