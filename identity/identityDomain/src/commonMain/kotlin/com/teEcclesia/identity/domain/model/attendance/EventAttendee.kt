package com.teEcclesia.identity.domain.model.attendance

import com.teEcclesia.identity.domain.model.UserRole
import kotlinx.datetime.LocalDateTime

data class EventAttendee(
    val id: Long,
    val eventId: Long,
    val userId: String,
    val name: String,
    val role: UserRole,
    val stageName: String? = null,
    val yearName: String? = null,
    val registeredAt: LocalDateTime
)
