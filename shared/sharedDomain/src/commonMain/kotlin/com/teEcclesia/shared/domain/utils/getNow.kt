package com.teEcclesia.shared.domain.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getToday(): LocalDate {
    return Clock.System.now().toLocalDateTime(currentSystemDefault()).date
}

@OptIn(ExperimentalTime::class)
fun getNow(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(currentSystemDefault())
}