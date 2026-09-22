package com.teEcclesia.shared.domain.utils

import kotlinx.datetime.LocalTime

fun parseTime(stringTime: String) = runCatching { LocalTime.parse(stringTime) }.getOrNull()
