package com.teEcclesia.shared.domain.utils.validation

enum class PasswordValidationError {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_LOWERCASE,
    NO_DIGIT,
    NO_SPECIAL_CHAR
}

private val ALLOWED_SPECIAL_CHARS = setOf('@', '#', '$', '%', '^', '&', '+', '=', '!')

fun getPasswordValidationError(password: String): PasswordValidationError? {
    if (password.length < 8) return PasswordValidationError.TOO_SHORT
    if (!password.any { it.isDigit() }) return PasswordValidationError.NO_DIGIT
    if (!password.any { it.isLowerCase() }) return PasswordValidationError.NO_LOWERCASE
    if (!password.any { it.isUpperCase() }) return PasswordValidationError.NO_UPPERCASE
    if (!password.any { it in ALLOWED_SPECIAL_CHARS }) return PasswordValidationError.NO_SPECIAL_CHAR
    return null
}