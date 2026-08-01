package com.teEcclesia.shared.domain.utils.validation

fun isValidCodeInput(value: String): Boolean {
    val regex = Regex("^[A-Za-z]?[0-9]{0,8}$")
    return regex.matches(value)
}
