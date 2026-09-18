package com.teEcclesia.shared.domain.utils.validation

fun validatePhone(phone: String): Boolean {
    val trimmed = phone.trim()
    val egyptPhoneRegex = "^(?:\\+2)?(?:010|011|012|015)\\d{8}$".toRegex()
    val internationalPhoneRegex = "^\\+[1-9]\\d{6,14}$".toRegex()
    return egyptPhoneRegex.matches(trimmed) || internationalPhoneRegex.matches(trimmed)
}
