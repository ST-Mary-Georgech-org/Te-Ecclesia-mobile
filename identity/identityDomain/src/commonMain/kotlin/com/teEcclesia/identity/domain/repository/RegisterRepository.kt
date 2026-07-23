package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.CompleteProfileRequest
import com.teEcclesia.identity.domain.model.InitiateWhatsAppVerificationResponse
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.RegisterResponse
import com.teEcclesia.identity.domain.model.TokenResponse
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData

interface RegisterRepository {
    suspend fun register(request: RegisterRequest, imageBytes: ByteArray?, certificateImageBytes: ByteArray?): TokenResponse
    suspend fun completeProfile(request: CompleteProfileRequest, certificateImageBytes: ByteArray?): RegisterResponse
    suspend fun verifyEmail(email: String, otp: String, deviceToken: String?)
    suspend fun initiateWhatsAppVerification(phone: String): InitiateWhatsAppVerificationResponse
    suspend fun getWhatsAppStatus(token: String)
    suspend fun getConfessionPriests(pageQuery: PageQuery): PagedData<Priest>
    suspend fun searchParent(query: String): UserSummary?
    suspend fun searchMakhdoom(query: String): UserSummary?
}
