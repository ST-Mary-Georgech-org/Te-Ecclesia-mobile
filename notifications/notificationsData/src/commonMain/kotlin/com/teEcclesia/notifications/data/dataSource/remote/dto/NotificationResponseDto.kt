package com.teEcclesia.notifications.data.dataSource.remote.dto

import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.model.NotificationType
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("message")
    val message: String,
    @SerialName("type")
    val type: NotificationType,
    @SerialName("sentAt")
    val sentAt: String,
    @SerialName("read")
    val isRead: Boolean
)

fun NotificationResponseDto.toDomain() = NotificationResponse(
    id = id,
    title = title,
    message = message,
    type = type,
    sentAt = sentAt.toLocalDateTimeOrDefault(),
    isRead = isRead
)
