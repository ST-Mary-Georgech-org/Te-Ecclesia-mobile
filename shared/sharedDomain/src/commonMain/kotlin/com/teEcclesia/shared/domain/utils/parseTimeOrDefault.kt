package com.teEcclesia.shared.domain.utils

import kotlinx.datetime.LocalTime

fun parseTimeOrDefault(stringTime: String) = runCatching { LocalTime.parse(stringTime) }.getOrDefault(LocalTime(0, 0))
