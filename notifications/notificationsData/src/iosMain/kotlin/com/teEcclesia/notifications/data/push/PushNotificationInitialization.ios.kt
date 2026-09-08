package com.teEcclesia.notifications.data.push

import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush

actual fun onPlatformPushNotificationInitialization(showPushNotification: Boolean) {
    KMPNotifier.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = showPushNotification,
            askNotificationPermissionOnStart = true,
        ),
        extensions = listOf(FirebasePush),
    )
}
