package com.teEcclesia.notifications.data.dataSource.remote.dto

import com.teEcclesia.notifications.domain.model.AdminSendNotificationParam
import com.teEcclesia.shared.domain.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminSendNotificationRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("body")
    val body: String,
    @SerialName("userIds")
    val userIds: List<String>?,
    @SerialName("role")
    val role: UserRole?,
    @SerialName("educationalStageId")
    val educationalStageId: Long?,
    @SerialName("dataPayload")
    val dataPayload: Map<String, String>?
)

fun AdminSendNotificationParam.toDto(): AdminSendNotificationRequestDto = AdminSendNotificationRequestDto(
    title = title.trim(),
    body = body.trim(),
    userIds = userIds?.ifEmpty { null },
    role = role,
    educationalStageId = educationalStageId,
    dataPayload = dataPayload?.ifEmpty { null }
)

