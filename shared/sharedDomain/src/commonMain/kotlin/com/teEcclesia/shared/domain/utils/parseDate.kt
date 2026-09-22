package com.teEcclesia.shared.domain.utils

import kotlinx.datetime.LocalDate

fun parseDate(stringDate: String) = runCatching { LocalDate.parse(stringDate) }.getOrNull()
