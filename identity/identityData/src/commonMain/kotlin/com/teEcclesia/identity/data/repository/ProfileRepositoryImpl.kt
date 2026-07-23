package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.ProfileResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ProfileRepositoryImpl(
    client: HttpClient
) : BaseRepository(client), ProfileRepository {

    override suspend fun getRegistrationProfile(): ProfileResponse {
        val response = tryToExecute<ProfileResponseDto> {
            get(GET_ME_ENDPOINT)
        }
        
        return response.toDomain()
    }

    companion object {
        const val GET_ME_ENDPOINT = "api/v1/identity/auth/me"
    }
}
