package com.teEcclesia.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.home.api.HomeFeatureApi
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.IdentityFeatureApi
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    backStack: NavBackStack<NavKey>,
    identityFeatureApi: IdentityFeatureApi = koinInject(),
    homeFeatureApi: HomeFeatureApi = koinInject(),
) {
    NavDisplay(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = DialogSceneStrategy(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            val initialIndex = getNavigationIndex(initialState.key)
            val targetIndex = getNavigationIndex(targetState.key)

            val isReverse =
                initialIndex != -1 && targetIndex != -1 && targetIndex < initialIndex

            if (isReverse) {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            } else {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } + fadeIn() togetherWith
                    slideOutHorizontally { it } + fadeOut()
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { -it } + fadeIn() togetherWith
                    slideOutHorizontally { it } + fadeOut()
        },
        entryProvider = remember {
            identityFeatureApi() + homeFeatureApi()
        },
    )
}

private operator fun <T : Any> ((T) -> NavEntry<T>).plus(
    other: (T) -> NavEntry<T>
): (T) -> NavEntry<T> = { key ->
    try {
        this(key)
    } catch (_: IllegalStateException) {
        other(key)
    }
}

private fun getNavigationIndex(route: Any?): Int {
    val routeStr = route?.toString() ?: ""
    return when {
        routeStr.contains(HomeRoute::class.simpleName.orEmpty()) -> 0
        else -> -1
    }
}
