package com.teEcclesia.shared.domain.utils.validation

fun isValidPhoneInput(phone: String): Boolean {
    val inputRegex = "^(0|01|01[0125]\\d{0,8}|\\+\\d{0,15})?$".toRegex()
    return inputRegex.matches(phone)
}