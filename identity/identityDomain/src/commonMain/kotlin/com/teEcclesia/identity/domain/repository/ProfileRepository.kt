package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.ProfileResponse

interface ProfileRepository {
    suspend fun getRegistrationProfile(): ProfileResponse
}
