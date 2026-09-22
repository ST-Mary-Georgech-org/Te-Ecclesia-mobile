package com.teEcclesia.identity.domain.model.attendance

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class ServiceRepeatedEvent(
    val id: Long,
    val serviceId: Long,
    val name: String?,
    val startDate: LocalDate,
    val nextCreationDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val repeatEvery: Int,
    val createdAt: LocalDateTime
)
