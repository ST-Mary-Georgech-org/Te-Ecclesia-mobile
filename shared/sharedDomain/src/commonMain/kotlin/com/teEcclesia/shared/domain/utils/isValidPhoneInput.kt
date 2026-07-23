package com.teEcclesia.shared.domain.utils

fun isValidPhoneInput(phone: String): Boolean {
    val inputRegex = "^(0|01|01[0125]\\d{0,8})?$".toRegex()
    return inputRegex.matches(phone)
}