package com.teEcclesia.notifications.domain.model

import com.teEcclesia.shared.domain.model.UserRole

data class AdminSendNotificationParam(
    val title: String,
    val body: String,
    val userIds: List<String>?,
    val role: UserRole?,
    val educationalStageId: Long?,
    val dataPayload: Map<String, String>?
)
