package com.teEcclesia.identity.domain.model.attendance

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class UserAttendanceHistory(
    val id: Long,
    val eventId: Long,
    val serviceId: Long,
    val serviceName: String,
    val eventName: String?,
    val eventDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val registeredAt: LocalDateTime
)
