package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.ApproveUserRequest
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PagedData

interface ProfileRepository {
    suspend fun getRegistrationProfile(): ProfileResponse
    suspend fun getUserProfile(userId: String): ProfileResponse
    suspend fun getRegistrationRequests(
        role: UserRole?, 
        search: String?, 
        page: Int, 
        size: Int, 
        sortBy: String, 
        sortOrder: String
    ): PagedData<ProfileResponse>
    suspend fun approveUser(
        userId: String, 
        request: ApproveUserRequest? = null,
        imageBytes: ByteArray? = null,
        identityDocumentBytes: ByteArray? = null,
        ordinationCertificateBytes: ByteArray? = null
    )
    suspend fun updateUser(
        userId: String, 
        request: ApproveUserRequest,
        imageBytes: ByteArray? = null,
        identityDocumentBytes: ByteArray? = null,
        ordinationCertificateBytes: ByteArray? = null
    )
    suspend fun rejectUser(userId: String, reason: String)
    suspend fun createMakhdoomDirectly(
        request: RegisterRequest,
        imageBytes: ByteArray?,
        identityDocumentBytes: ByteArray?
    )
    suspend fun getApprovedUsers(
        search: String?,
        stageId: Long?,
        yearId: Long?,
        role: UserRole?,
        page: Int,
        size: Int = 20,
        sortBy: String = "createdAt",
        sortOrder: String = "DESC"
    ): PagedData<ProfileResponse>
}

