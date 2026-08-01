package com.teEcclesia.shared.domain.utils

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant

fun LocalDate.toLocalDateTime() = this.atTime(0, 0)

fun LocalDateTime.toUtcInstant(): Instant {
    return this.toInstant(TimeZone.currentSystemDefault())
}