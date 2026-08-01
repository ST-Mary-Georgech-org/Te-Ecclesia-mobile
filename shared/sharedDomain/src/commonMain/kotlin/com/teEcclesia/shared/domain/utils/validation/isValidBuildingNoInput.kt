package com.teEcclesia.shared.domain.utils.validation

fun isValidBuildingNoInput(value: String): Boolean {
    val regex = Regex("^[0-9\\u0660-\\u0669]{0,3}[\\u0621-\\u064A]?$")
    return regex.matches(value)
}
