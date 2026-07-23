package com.teEcclesia.shared.domain.utils

fun isValidFinalEmail(email: String): Boolean {
    // Standard RFC-compliant email regex
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return email.trim().matches(emailRegex)
}