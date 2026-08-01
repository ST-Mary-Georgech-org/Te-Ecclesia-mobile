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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.push.PushListener
import com.mmk.kmpnotifier.push.firebase.addPushListener
import com.teEcclesia.appEntryPoint.EntryPoint
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.util.NotificationClickState
import com.teEcclesia.util.SetSystemBarsAppearance
import com.teEcclesia.util.toStringMap
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.coroutines.cancellation.CancellationException


@Preview
@Composable
fun App(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    settingsRepository: SettingsRepository = koinInject(),
    appLocalizer: AppLocalizer = koinInject(),
    authenticationRepository: AuthenticationRepository = koinInject(),
    effector: Effector = koinInject()
) {
    val currentTheme by settingsRepository.observeAppTheme().collectAsStateWithLifecycle()
    val currentLanguage by settingsRepository.observeAppLanguage().collectAsStateWithLifecycle()

    val isDarkTheme = when (currentTheme) {
        AppTheme.SYSTEM -> isSystemDarkTheme
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }

    val coroutineScope = rememberCoroutineScope()
    val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    LaunchedEffect(Unit) {
        KMPNotifier.addPushListener(object : PushListener {
            override fun onNewToken(token: String) {
                coroutineScope.launch(exceptionHandler) {
                    try {
                        authenticationRepository.updateDeviceToken(token)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                    }
                }
            }

            override fun onPushNotificationWithPayloadData(
                title: String?,
                body: String?,
                data: PayloadData
            ) {
                KMPNotifier.localNotifier.notify {
                    this.title = title.orEmpty()
                    this.body = body.orEmpty()
                    this.payloadData = data.toStringMap()
                }
            }
        })

        NotificationClickState.consumePendingPayload()

        coroutineScope.launch {
            NotificationClickState.clickFlow.collect {
                NotificationClickState.consumePendingPayload()
            }
        }
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

