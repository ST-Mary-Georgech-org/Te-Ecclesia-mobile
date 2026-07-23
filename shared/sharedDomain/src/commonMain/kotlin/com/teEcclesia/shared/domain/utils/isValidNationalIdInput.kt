package com.teEcclesia.shared.domain.utils

fun isValidNationalIdInput(input: String): Boolean {
    val inputRegex = "^([23]\\d{0,13})?$".toRegex()
    return inputRegex.matches(input)
}