package com.teEcclesia.shared.domain.utils

import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDateTimeOrDefault(): LocalDateTime {
    return runCatching {
        Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
    }.getOrElse {
        LocalDateTime(1999, 1, 1, 0, 0)
    }
}
