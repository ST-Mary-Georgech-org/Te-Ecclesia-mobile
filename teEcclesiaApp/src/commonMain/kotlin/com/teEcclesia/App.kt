package com.teEcclesia

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.appEntryPoint.EntryPoint
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.notifications.data.push.PushNotificationsInitializer
import com.teEcclesia.notifications.data.util.NotificationClickState
import com.teEcclesia.util.SetSystemBarsAppearance
import com.teEcclesia.util.handleNotificationClick
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.compose.koinInject

@Preview
@Composable
fun App(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    settingsRepository: SettingsRepository = koinInject(),
    appLocalizer: AppLocalizer = koinInject(),
    effector: Effector = koinInject()
) {
    val coroutineScope = rememberCoroutineScope()
    val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    val urlHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        PushNotificationsInitializer.listenToNotifications(coroutineScope, exceptionHandler)

        NotificationClickState.consumePendingPayload()?.let { data ->
            handleNotificationClick(data, coroutineScope, exceptionHandler, effector, urlHandler::openUri)
        }

        NotificationClickState.clickFlow.collect { data ->
            handleNotificationClick(data, coroutineScope, exceptionHandler, effector, urlHandler::openUri)
        }
    }

    val currentTheme by settingsRepository.observeAppTheme().collectAsStateWithLifecycle()
    val currentLanguage by settingsRepository.observeAppLanguage().collectAsStateWithLifecycle()

    val isDarkTheme = when (currentTheme) {
        AppTheme.SYSTEM -> isSystemDarkTheme
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }

    val languageCode = if (currentLanguage == AppLanguage.DEFAULT) {
        appLocalizer.getDeviceLanguageIso()
    } else {
        currentLanguage.iso
    }

    Theme(
        language = languageCode,
        darkTheme = isDarkTheme,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                SetSystemBarsAppearance(currentTheme)
                EntryPoint()
            }
        }
    )
}
