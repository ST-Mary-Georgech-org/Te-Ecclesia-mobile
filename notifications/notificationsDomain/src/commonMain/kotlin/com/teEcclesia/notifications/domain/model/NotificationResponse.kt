package com.teEcclesia.notifications.domain.model

import kotlinx.datetime.LocalDateTime

data class NotificationResponse(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val sentAt: LocalDateTime,
    val isRead: Boolean
)
