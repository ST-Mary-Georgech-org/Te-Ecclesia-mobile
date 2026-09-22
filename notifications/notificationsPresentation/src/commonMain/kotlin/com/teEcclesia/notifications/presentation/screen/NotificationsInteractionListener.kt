package com.teEcclesia.notifications.presentation.screen

import com.teEcclesia.notifications.domain.model.NotificationResponse

interface NotificationsInteractionListener {
    fun onClickBack()
    fun onLoadMoreNotifications()
    fun onReload()
    fun onDeleteNotification(notification: NotificationResponse)
    fun onNotificationClick(notification: NotificationResponse)
}
