package com.teEcclesia.designsystem.util.extentions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

private val timeFormat24 = LocalTime.Format {
    hour(padding = Padding.ZERO)
    char(':')
    minute(padding = Padding.ZERO)
}

fun LocalTime?.format(): String = this?.format(timeFormat24).orEmpty()

fun LocalDate?.format(): String = this?.format(LocalDate.Formats.ISO).orEmpty()

fun LocalDateTime?.format(): String = this?.let {
    "${it.date.format()} ${it.time.format()}"
}.orEmpty()