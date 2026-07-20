package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.CompleteProfileRequest
import com.teEcclesia.identity.domain.model.InitiateWhatsAppVerificationResponse
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.RegisterResponse
import com.teEcclesia.identity.domain.model.TokenResponse

interface RegisterRepository {
    suspend fun register(request: RegisterRequest, imageBytes: ByteArray?, certificateImageBytes: ByteArray?): TokenResponse
    suspend fun completeProfile(request: CompleteProfileRequest, certificateImageBytes: ByteArray?): RegisterResponse
    suspend fun verifyEmail(email: String, otp: String, deviceToken: String?)
    suspend fun initiateWhatsAppVerification(phone: String): InitiateWhatsAppVerificationResponse
    suspend fun getWhatsAppStatus(token: String)
}
