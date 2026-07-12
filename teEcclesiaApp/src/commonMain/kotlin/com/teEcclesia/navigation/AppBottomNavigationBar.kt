package com.teEcclesia.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import com.teEcclesia.appEntryPoint.MainEntryInteractionListener
import com.teEcclesia.designsystem.components.bottomNavigation.BottomNavigationBar
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.ProfileRoute
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.home
import teecclesia.designsystem.generated.resources.ic_home
import teecclesia.designsystem.generated.resources.ic_home_selected
import teecclesia.designsystem.generated.resources.ic_profile
import teecclesia.designsystem.generated.resources.profile

@Composable
fun BoxScope.AppBottomNavigationBar(
    showBottomNavigation: Boolean,
    activeRoute: NavKey?,
    interactionListener: MainEntryInteractionListener
) {
    val animationSpec = tween<Float>(easing = EaseOut)
    val animationSpecs = tween<IntOffset>(easing = EaseOut)

    AnimatedVisibility(
        showBottomNavigation,
        enter = fadeIn(animationSpec) + slideInVertically(animationSpecs) { it },
        exit = fadeOut(animationSpec) + slideOutVertically(animationSpecs) { it },
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BottomNavigationBar(
                selectedItemIndex = getSelectedNavigationIndex(activeRoute),
            ) {
                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = {
                        interactionListener.resetToRoute(HomeRoute)
                    }
                )


                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_profile),
                    notSelectedIcon = painterResource(Res.drawable.ic_profile),
                    title = Res.string.profile.asString(),
                    entry = {
                        interactionListener.resetToRoute(ProfileRoute)
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.colorScheme.background.secondary)
                    .navigationBarsPadding()
            )
        }
    }
}

private fun getSelectedNavigationIndex(route: NavKey?): Int {
    return when (route) {
        is HomeRoute -> 0
        is ProfileRoute -> 1
        else -> -1
    }
}
