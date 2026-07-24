package com.teEcclesia.shared.domain.utils.validation

fun validatePhone(phone: String): Boolean {
    val phoneRegex = "^(?:\\+2)?(?:010|011|012|015)\\d{8}$".toRegex()
    return phoneRegex.matches(phone)
}
