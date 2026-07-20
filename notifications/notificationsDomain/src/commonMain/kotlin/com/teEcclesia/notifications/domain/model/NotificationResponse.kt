package com.teEcclesia.notifications.domain.model

data class NotificationResponse(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val sentAt: String,
    val isRead: Boolean
)
