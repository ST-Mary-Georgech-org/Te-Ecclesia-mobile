package com.teEcclesia.shared.domain.utils.validation

fun validateArabicName(name: String): Boolean {
    val arabicRegex = Regex("^[\\u0621-\\u063A\\u0641-\\u064A]+$")
    return arabicRegex.matches(name)
}