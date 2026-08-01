package com.teEcclesia.shared.domain.utils.validation

fun isValidApartmentInput(value: String): Boolean {
    val regex = Regex("^[0-9\\u0660-\\u0669]{0,3}$")
    return regex.matches(value)
}
