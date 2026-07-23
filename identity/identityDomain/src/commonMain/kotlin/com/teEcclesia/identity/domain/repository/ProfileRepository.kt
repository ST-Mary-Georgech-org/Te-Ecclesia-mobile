package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PagedData

interface ProfileRepository {
    suspend fun getRegistrationProfile(): ProfileResponse
    suspend fun getRegistrationRequests(
        role: UserRole?, 
        search: String?, 
        page: Int, 
        size: Int, 
        sortBy: String, 
        sortOrder: String
    ): PagedData<ProfileResponse>
    suspend fun approveUser(userId: String)
    suspend fun rejectUser(userId: String, reason: String)
}
