package com.teEcclesia.notifications.data.dataSource.remote.dto

import com.teEcclesia.notifications.domain.model.UnreadCountResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadCountResponseDto(
    @SerialName("unreadCount")
    val unreadCount: Long
)

fun UnreadCountResponseDto.toDomain() = UnreadCountResponse(
    unreadCount = unreadCount
)
