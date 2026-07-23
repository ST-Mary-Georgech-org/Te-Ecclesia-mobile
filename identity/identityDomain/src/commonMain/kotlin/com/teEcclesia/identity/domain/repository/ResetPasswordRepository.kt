package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.ForgotPasswordResponse
import com.teEcclesia.identity.domain.model.VerificationMethod

interface ResetPasswordRepository {
    suspend fun requestOTP(key: String, method: VerificationMethod): ForgotPasswordResponse?
    suspend fun verifyOTPCode(key: String, otp: String, method: VerificationMethod, deviceToken: String?)
    suspend fun resetPassword(key: String, otp: String, newPassword: String, method: VerificationMethod)
    suspend fun reSendOtp(key: String, method: VerificationMethod): ForgotPasswordResponse?
}
