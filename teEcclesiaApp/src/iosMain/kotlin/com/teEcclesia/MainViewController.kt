package com.teEcclesia

import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}

fun onApplicationStart() {
    KMPNotifier.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = true,
            askNotificationPermissionOnStart = true
        ),
        FirebasePush
    )
}
