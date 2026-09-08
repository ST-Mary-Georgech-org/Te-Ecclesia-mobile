package com.teEcclesia.notifications.data.push

import android.content.Context
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush
import org.koin.mp.KoinPlatform

actual fun onPlatformPushNotificationInitialization(showPushNotification: Boolean) {
    val context = KoinPlatform.getKoin().getOrNull<Context>()
    val iconResId = context?.applicationInfo?.icon ?: android.R.drawable.ic_dialog_info
    KMPNotifier.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = iconResId,
            showPushNotification = showPushNotification,
        ),
        extensions = listOf(FirebasePush),
    )
}
