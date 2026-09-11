package com.teEcclesia.appEntryPoint

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.teEcclesia.designsystem.components.snackbar.AnimatedSnackBar
import com.teEcclesia.designsystem.navigation.effector.Effect
import com.teEcclesia.designsystem.navigation.effector.EffectHandler
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.AttendanceServicesRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.logging.CrashLogger
import com.teEcclesia.navigation.AppBottomNavigationBar
import com.teEcclesia.navigation.NavigationRoot
import com.teEcclesia.navigation.replaceAll
import com.teEcclesia.shared.domain.push.NotificationPermissionHandler
import com.teEcclesia.util.buildNavigationSerializerConfig
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EntryPoint(
    viewModel: MainEntryViewModel = koinViewModel(),
    authorizationService: AuthorizationService = koinInject(),
    effector: Effector = koinInject(),
    crashLogger: CrashLogger = koinInject(),
    permissionHandler: NotificationPermissionHandler = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by authorizationService.observeAuthState().collectAsStateWithLifecycle()
    val accessToken by authorizationService.observeAccessToken().collectAsStateWithLifecycle()

    var isNotificationPermissionGranted by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionHandler.checkPermission { isGranted ->
                    val previouslyDenied = !isNotificationPermissionGranted
                    isNotificationPermissionGranted = isGranted
                    if (previouslyDenied && isGranted) {
                        viewModel.syncPushTokenIfLoggedIn()
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        permissionHandler.checkPermission { isGranted ->
            isNotificationPermissionGranted = isGranted
            if (isGranted) {
                viewModel.syncPushTokenIfLoggedIn()
            }
        }
    }

    val navigationSerializerConfig = remember { buildNavigationSerializerConfig() }
    val backStack = rememberNavBackStack(navigationSerializerConfig,
        viewModel.getStaticInitialRoute(authState))
    val currentRoute = backStack.lastOrNull()

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            val screenName = route::class.simpleName ?: route.toString()
            crashLogger.setCustomKey("current_screen", screenName)
            crashLogger.log("Navigated to screen: $screenName ($route)")
        }
    }

    EffectHandler(effector.effect) { effect ->
        when (effect) {
            is Effect.Navigate -> {
                if (effect.route != currentRoute) backStack.add(effect.route)
            }

            is Effect.PopBackStack -> {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            }

            is Effect.ResetTo -> {
                backStack.replaceAll(listOf(effect.route))
            }

            is Effect.ResetToMultiple -> {
                backStack.replaceAll(effect.routes)
            }
        }
    }

    val showBottomNavigation = currentRoute is HomeRoute
            || currentRoute is ProfileRoute
            || currentRoute is RegistrationRequestsRoute
            || currentRoute is AttendanceServicesRoute

    LaunchedEffect(authState, accessToken) {
        viewModel.handleAuthState(authState, currentRoute)
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

        val hasRequestsAccess by authorizationService.observeRequestsAccess().collectAsStateWithLifecycle()
        val hasAttendanceAccess by authorizationService.observeAttendanceAccess().collectAsStateWithLifecycle()
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            NavigationRoot(backStack)

            AppBottomNavigationBar(
                showBottomNavigation = showBottomNavigation,
                activeRoute = currentRoute,
                hasRequestsAccess = hasRequestsAccess,
                hasAttendanceAccess = hasAttendanceAccess,
                interactionListener = viewModel
            )
        }
    }
}
