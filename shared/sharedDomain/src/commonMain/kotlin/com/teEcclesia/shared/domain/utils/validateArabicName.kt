package com.teEcclesia.shared.domain.utils

fun validateArabicName(name: String): Boolean {
    val arabicRegex = Regex("^[\\u0600-\\u06FF]+$")
    return arabicRegex.matches(name)
}
