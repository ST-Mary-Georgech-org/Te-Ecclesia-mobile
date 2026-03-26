package com.teEcclesia.appEntryPoint

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.bottomNavigation.BottomNavigationBar
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.home.api.HomeFeatureApi
import com.teEcclesia.identity.api.IdentityFeatureApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.home
import teecclesia.designsystem.generated.resources.ic_home
import teecclesia.designsystem.generated.resources.ic_home_selected

@Composable
fun LoggedInContainer(
    state: MainEntryState,
    listener: MainEntryInteractionListener,
) {
    val animationSpec = tween<Float>(easing = EaseOut)
    val animationSpecs = tween<IntOffset>(easing = EaseOut)
    val bottomPadding by animateDpAsState(
        targetValue = if (state.showBottomNavigation) 74.dp else 0.dp,
        animationSpec = tween(easing = EaseOut)
    )

    Box(
        modifier = Modifier
            .background(Theme.colorScheme.background.secondary)
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        FeatureContent(
            activeFeature = state.activeFeature,
            modifier = Modifier.padding(bottom = bottomPadding),
            updateBottomNavigationVisibility = listener::onBottomNavigationChanged,
            showSnackBar = listener::showSnackBar
        )

        AnimatedVisibility(
            state.showBottomNavigation,
            enter = fadeIn(animationSpec) + slideInVertically(animationSpecs) { it },
            exit = fadeOut(animationSpec) + slideOutVertically(animationSpecs) { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigationBar(
                selectedItemIndex = getSelectedNavigationIndex(state.activeFeature),
            ) {
                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = { listener.setActiveFeature(Feature.Home) }
                )

                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = { listener.setActiveFeature(Feature.Categories) }
                )

                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = { listener.setActiveFeature(Feature.Stats) }
                )

                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = { listener.setActiveFeature(Feature.ChatBot) }
                )

                bottomNavigationItem(
                    selectedIcon = painterResource(Res.drawable.ic_home_selected),
                    notSelectedIcon = painterResource(Res.drawable.ic_home),
                    title = Res.string.home.asString(),
                    entry = { listener.setActiveFeature(Feature.Profile) }
                )
            }
        }
    }
}

@Composable
private fun FeatureContent(
    activeFeature: Feature,
    identityApi: IdentityFeatureApi = koinInject(),
    homeApi: HomeFeatureApi = koinInject(),
    categoriesApi: HomeFeatureApi = koinInject(),
    statsApi: HomeFeatureApi = koinInject(),
    chatBotApi: HomeFeatureApi = koinInject(),
    paymentsApi: HomeFeatureApi = koinInject(),
    modifier: Modifier = Modifier,
    updateBottomNavigationVisibility: (Boolean) -> Unit = {},
    showSnackBar: (String, String?, Boolean, Painter?, Long?, Color) -> Unit
) {
    Box(modifier) {
        Crossfade(targetState = activeFeature) { feature ->
            when (feature) {
                Feature.Home -> homeApi.TabEntry(updateBottomNavigationVisibility)
                Feature.Categories -> categoriesApi.TabEntry(updateBottomNavigationVisibility)
                Feature.Stats -> statsApi.TabEntry(updateBottomNavigationVisibility)
                Feature.ChatBot -> chatBotApi.TabEntry(updateBottomNavigationVisibility)
                Feature.Profile -> identityApi.TabEntry(updateBottomNavigationVisibility, showSnackBar)
                Feature.Payments -> paymentsApi.TabEntry(updateBottomNavigationVisibility)
            }
        }
    }
}

private fun getSelectedNavigationIndex(activeFeature: Feature): Int {
    return when (activeFeature) {
        Feature.Home -> 0
        Feature.Categories -> 1
        Feature.Stats -> 2
        Feature.ChatBot -> 3
        Feature.Profile -> 4
        Feature.Payments -> -1 // Not in bottom nav
    }
}