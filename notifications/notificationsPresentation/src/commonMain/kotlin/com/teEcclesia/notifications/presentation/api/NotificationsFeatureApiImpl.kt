package com.teEcclesia.notifications.presentation.api

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.teEcclesia.notifications.api.NotificationsFeatureApi
import com.teEcclesia.notifications.api.NotificationsRoute
import com.teEcclesia.notifications.api.SendNotificationRoute
import com.teEcclesia.notifications.presentation.screen.NotificationsScreen
import com.teEcclesia.notifications.presentation.screen.adminSend.AdminSendNotificationScreen

class NotificationsFeatureApiImpl : NotificationsFeatureApi {
    override fun invoke(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<NotificationsRoute> { NotificationsScreen() }
            entry<SendNotificationRoute> { AdminSendNotificationScreen() }
        }
    }
}
