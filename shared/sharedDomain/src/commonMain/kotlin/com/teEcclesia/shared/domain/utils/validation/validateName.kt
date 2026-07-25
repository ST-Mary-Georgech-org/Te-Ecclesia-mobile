package com.teEcclesia.shared.domain.utils.validation

fun validateName(fullName: String): Boolean {
    val parts = fullName.trim().split(" ")
    return (parts.size < 2 || parts.any { it.length < 2 }).not()
}
