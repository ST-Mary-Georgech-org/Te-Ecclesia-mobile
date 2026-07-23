package com.teEcclesia.shared.domain.utils

fun validateUsername(username: String): Boolean {
    val trimmed = username.trim()
    if (trimmed.isEmpty()) return false

    // Phone: 11 digits starting with 01
    val phoneRegex = "^01[0125][0-9]{8}$".toRegex()
    if (phoneRegex.matches(trimmed)) return true

    // National ID: 14 digits
    val nationalIdRegex = "^[0-9]{14}$".toRegex()
    if (nationalIdRegex.matches(trimmed)) return true

    // Code: 9 characters, 1 letter followed by 8 numbers (e.g. A12345678)
    val codeRegex = "^[a-zA-Z][0-9]{8}$".toRegex()
    if (codeRegex.matches(trimmed)) return true

    // Email format
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    if (emailRegex.matches(trimmed)) return true

    // General fallback identifier: alphanumeric, 3-30 chars
    val generalRegex = "^[a-zA-Z0-9_.@-]{3,50}$".toRegex()
    return generalRegex.matches(trimmed)
}
