package com.teEcclesia.identity.domain.useCase.validation.auth


class ValidationUseCase {
    private val maxFileSize = 5 * 1024 * 1024 // 5 MB

    fun validatePhone(phone: String): Boolean {
        return phoneRegex.matches(phone)
    }

    fun validateName(fullName: String): Boolean {
        val parts = fullName.trim().split(" ")
        return (parts.size < 2 || parts.any { it.length < 2 }).not()
    }

    fun validatePassword(password: String): Boolean {
        return passwordRegex.matches(password)
    }

    fun validateOtp(otp: String): Boolean {
        return otpRegex.matches(otp)
    }

    fun validateFileSizes(file: ByteArray): Boolean {
        return file.size <= maxFileSize
    }

    fun validateUsername(username: String): Boolean {
        return usernameRegex.matches(username)
    }

    private companion object {
        val phoneRegex = "^(?:\\+2)?(?:010|011|012|015)\\d{8}$".toRegex()
        val otpRegex = "^\\d{4}$".toRegex()
        val passwordRegex =
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$""".toRegex()
        val usernameRegex = "^[a-zA-Z0-9_]{3,16}$".toRegex()
    }
}
