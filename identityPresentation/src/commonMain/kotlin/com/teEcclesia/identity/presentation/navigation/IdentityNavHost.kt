package com.teEcclesia.identity.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.compose.rememberNavController
import com.teEcclesia.designsystem.utils.asStringSuspend
import com.teEcclesia.identity.presentation.navigation.effector.Effect
import com.teEcclesia.identity.presentation.navigation.effector.EffectHandler
import com.teEcclesia.identity.presentation.navigation.effector.Effector
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun IdentityNavHost(
    updateBottomNavigationVisibility: (Boolean) -> Unit = {},
    effector: Effector = koinInject(),
    startDestination: BaseRoute = HomeRoute,
    showSnackBar: (String, String?, Boolean, Painter?, Long?, Color) -> Unit = { _, _, _, _, _, _ -> }
) {
    val navController = rememberNavController()

    EffectHandler(effector.effect) { effect ->
        when (effect) {
            is Effect.Navigate -> navController.navigate(
                route = effect.route, navOptions = effect.navOptions
            )

            is Effect.PopBackStack -> {
                effect.arguments.forEach { (key, value) ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(key, value)
                }
                navController.popBackStack()
            }


            is Effect.SetBackStackArgs -> {
                effect.arguments.forEach { (key, value) ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(key, value)
                }
            }

            is Effect.PopUpTo -> {
                navController.popBackStack(
                    route = effect.route,
                    inclusive = effect.inclusive,
                    saveState = effect.saveState
                )
            }

            is Effect.UpdateBottomNavigationVisibility -> {
                updateBottomNavigationVisibility(effect.isVisible)
            }

            is Effect.ShowSnackBar -> showSnackBar(
                effect.title.asStringSuspend(),
                effect.message.asStringSuspend(),
                effect.isSuccess,
                effect.customLeadingIcon,
                effect.duration,
                effect.iconTint
            )
        }
    }

    LaunchedEffect(Unit) {
        navController.currentBackStack.collectLatest {
            if (navController.currentDestination?.route in routesWithBottomNavigation) {
                updateBottomNavigationVisibility(true)
            } else {
                updateBottomNavigationVisibility(false)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        IdentityNavGraph(
            navController = navController,
            startDestination = startDestination,
            updateBottomNavigationVisibility = updateBottomNavigationVisibility
        )
    }
}

private val routesWithBottomNavigation = listOf(
    HomeRoute::class.qualifiedName,
    ProfileRoute::class.qualifiedName,
)