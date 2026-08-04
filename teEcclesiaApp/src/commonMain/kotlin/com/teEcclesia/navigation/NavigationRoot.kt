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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
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
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.notifications.api.NotificationsFeatureApi
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    backStack: NavBackStack<NavKey>,
    identityFeatureApi: IdentityFeatureApi = koinInject(),
    homeFeatureApi: HomeFeatureApi = koinInject(),
    notificationsFeatureApi: NotificationsFeatureApi = koinInject(),
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val dir = if (isRtl) -1 else 1
    NavDisplay(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background),
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        sceneStrategy = DialogSceneStrategy(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            val initialKey = initialState.key
            val targetKey = targetState.key

            val initialIndex = getNavigationIndex(initialKey)
            val targetIndex = getNavigationIndex(targetKey)

            val isReverse = if (initialIndex != -1 && targetIndex != -1) {
                targetIndex < initialIndex
            } else {
                val initialStackIndex = backStack.indexOfLast { it == initialKey }
                val targetStackIndex = backStack.indexOfLast { it == targetKey }
                targetStackIndex != -1 && targetStackIndex < initialStackIndex
            }

            if (isReverse) {
                slideInHorizontally { -it * dir } + fadeIn() togetherWith
                        slideOutHorizontally { it * dir } + fadeOut()
            } else {
                slideInHorizontally { it * dir } + fadeIn() togetherWith
                        slideOutHorizontally { -it * dir } + fadeOut()
            }
        },
        popTransitionSpec = {
            slideInHorizontally { -it * dir } + fadeIn() togetherWith
                    slideOutHorizontally { it * dir } + fadeOut()
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { -it * dir } + fadeIn() togetherWith
                    slideOutHorizontally { it * dir } + fadeOut()
        },
        entryProvider = remember {
            identityFeatureApi() + homeFeatureApi() + notificationsFeatureApi()
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
    val routeName = route?.toString() ?: return -1
    return when (routeName) {
        HomeRoute::class.simpleName -> 0
        RegistrationRequestsRoute::class.simpleName -> 1
        ProfileRoute::class.simpleName -> 2
        else -> -1
    }
}