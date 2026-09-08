package com.teEcclesia

import androidx.compose.ui.window.ComposeUIViewController
import com.teEcclesia.notifications.data.push.PushNotificationsInitializer

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}

fun onApplicationStart() {
    PushNotificationsInitializer.initialize(showPushNotification = true)
}

