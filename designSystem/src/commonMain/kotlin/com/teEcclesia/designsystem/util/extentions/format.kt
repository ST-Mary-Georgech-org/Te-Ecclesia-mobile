package com.teEcclesia.designsystem.util.extentions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun LocalDate?.format() = this?.let {
    val day = it.day.toString().padStart(2, '0')
    val month = it.month.number.toString().padStart(2, '0')
    val year = it.year.toString().padStart(4, '0')
    "$day/$month/$year"
}.orEmpty()
