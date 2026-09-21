package com.teEcclesia.identity.domain.model.attendance

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ServiceRepeatedEventRequest(
    val name: String?,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val repeatEvery: Int,
)
