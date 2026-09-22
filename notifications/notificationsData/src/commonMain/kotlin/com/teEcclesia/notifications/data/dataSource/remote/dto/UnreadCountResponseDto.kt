package com.teEcclesia.notifications.data.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadCountResponseDto(
    @SerialName("unreadCount")
    val unreadCount: Long
)
