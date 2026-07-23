package com.teEcclesia.shared.domain.utils

fun validateOtp(otp: String): Boolean {
    val otpRegex = "^\\d{4}$".toRegex()
    return otpRegex.matches(otp)
}
