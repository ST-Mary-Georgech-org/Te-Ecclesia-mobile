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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import com.teEcclesia.appEntryPoint.MainEntryInteractionListener
import com.teEcclesia.designsystem.components.bottomNavigation.BottomNavigationBar
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.api.AttendanceServicesRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.attendance
import teecclesia.designsystem.generated.resources.ic_document
import teecclesia.designsystem.generated.resources.ic_folder
import teecclesia.designsystem.generated.resources.ic_profile
import teecclesia.designsystem.generated.resources.profile
import teecclesia.designsystem.generated.resources.requests

@Composable
fun BoxScope.AppBottomNavigationBar(
    showBottomNavigation: Boolean,
    activeRoute: NavKey?,
    hasRequestsAccess: Boolean,
    hasAttendanceAccess: Boolean,
    interactionListener: MainEntryInteractionListener
) {
    val animationSpec = tween<Float>(easing = EaseOut)
    val animationSpecs = tween<IntOffset>(easing = EaseOut)

    var lastSelectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val targetIndex = getSelectedNavigationIndex(activeRoute, hasRequestsAccess, hasAttendanceAccess)

    val selectedIndex = if (targetIndex != -1) targetIndex else lastSelectedIndex

    LaunchedEffect(targetIndex) {
        if (targetIndex != -1) {
            lastSelectedIndex = targetIndex
        }
    }

    AnimatedVisibility(
        showBottomNavigation,
        enter = fadeIn(animationSpec) + slideInVertically(animationSpecs) { it },
        exit = fadeOut(animationSpec) + slideOutVertically(animationSpecs) { it },
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            key(hasRequestsAccess, hasAttendanceAccess) {
                BottomNavigationBar(
                    selectedItemIndex = selectedIndex,
                ) {
//                    bottomNavigationItem(
//                        selectedIcon = painterResource(Res.drawable.ic_home_selected),
//                        notSelectedIcon = painterResource(Res.drawable.ic_home),
//                        title = stringResource(Res.string.home),
//                        entry = {
//                            interactionListener.resetToRoute(HomeRoute)
//                        }
//                    )

                    if (hasAttendanceAccess) {
                        bottomNavigationItem(
                            selectedIcon = painterResource(Res.drawable.ic_document),
                            notSelectedIcon = painterResource(Res.drawable.ic_document),
                            title = stringResource(Res.string.attendance),
                            entry = {
                                interactionListener.resetToRoute(AttendanceServicesRoute)
                            }
                        )
                    }

                    if (hasRequestsAccess) {
                        bottomNavigationItem(
                            selectedIcon = painterResource(Res.drawable.ic_folder),
                            notSelectedIcon = painterResource(Res.drawable.ic_folder),
                            title = stringResource(Res.string.requests),
                            entry = {
                                interactionListener.resetToRoute(RegistrationRequestsRoute)
                            }
                        )
                    }

                    bottomNavigationItem(
                        selectedIcon = painterResource(Res.drawable.ic_profile),
                        notSelectedIcon = painterResource(Res.drawable.ic_profile),
                        title = stringResource(Res.string.profile),
                        entry = {
                            interactionListener.resetToRoute(ProfileRoute)
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Theme.colorScheme.background)
                    .navigationBarsPadding()
            )
        }
    }
}

private fun getSelectedNavigationIndex(
    route: NavKey?,
    hasRequestsAccess: Boolean,
    hasAttendanceAccess: Boolean
): Int {
    val items = mutableListOf<NavKey>()
//    items.add(HomeRoute)
    if (hasAttendanceAccess) items.add(AttendanceServicesRoute)
    if (hasRequestsAccess) items.add(RegistrationRequestsRoute)
    items.add(ProfileRoute)

    return items.indexOfFirst { it::class == route?.let { r -> r::class } }
}
