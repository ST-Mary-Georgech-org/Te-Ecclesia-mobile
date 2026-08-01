package com.teEcclesia.shared.domain.utils.validation

fun isValidNationalIdInput(input: String): Boolean {
    val inputRegex = "^([23][0-9]{0,13})?$".toRegex()
    return inputRegex.matches(input)
}