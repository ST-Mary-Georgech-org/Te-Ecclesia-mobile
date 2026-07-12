package com.teEcclesia.identity.domain.repository

interface ResetPasswordRepository {
    suspend fun requestOTP(phone: String)
    suspend fun verifyOTPCode(phone: String, otp: String)
    suspend fun resetPassword(phone: String, otp: String, newPassword: String)
    suspend fun reSendOtp(phone: String)
}
