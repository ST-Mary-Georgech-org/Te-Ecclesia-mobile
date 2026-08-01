package com.teEcclesia.shared.domain.utils.validation

fun isValidCodeFormat(value: String): Boolean {
    val regex = Regex("^[A-Za-z][0-9]{8}$")
    return regex.matches(value)
}
