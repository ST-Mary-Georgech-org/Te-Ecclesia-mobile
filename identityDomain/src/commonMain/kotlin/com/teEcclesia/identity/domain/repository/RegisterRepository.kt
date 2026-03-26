package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.RegisterRequest

interface RegisterRepository {
    suspend fun reSendOTP(phone: String)
    suspend fun verifyOTPCode(phone: String, otp: String)
    suspend fun register(request: RegisterRequest)
}