package com.teEcclesia.shared.domain.utils

import kotlinx.datetime.LocalTime

fun LocalTime.formatTime(): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}
