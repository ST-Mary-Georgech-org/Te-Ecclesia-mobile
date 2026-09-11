package com.teEcclesia.shared.domain.utils

import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDateTimeOrDefault(): LocalDateTime {
    return runCatching {
        Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
    }.recoverCatching {
        LocalDateTime.parse(this)
    }.recoverCatching {
        LocalDate.parse(this).atTime(0, 0)
    }.getOrElse {
        LocalDateTime(1999, 1, 1, 0, 0)
    }
}
