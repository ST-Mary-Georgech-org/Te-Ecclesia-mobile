package com.teEcclesia.identity.domain.model.attendance

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class ServiceEvent(
    val id: Long,
    val serviceId: Long,
    val name: String? = null,
    val eventDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val createdAt: LocalDateTime
)
