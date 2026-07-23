package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.ProfileResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.parameter
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.domain.utils.PagedData

class ProfileRepositoryImpl(
    client: HttpClient
) : BaseRepository(client), ProfileRepository {

    override suspend fun getRegistrationProfile(): ProfileResponse {
        val response = tryToExecute<ProfileResponseDto> {
            get(GET_ME_ENDPOINT)
        }
        
        return response.toDomain()
    }

    override suspend fun getRegistrationRequests(
        role: UserRole?,
        search: String?,
        page: Int,
        size: Int,
        sortBy: String,
        sortOrder: String
    ): PagedData<ProfileResponse> {
        val response = tryToExecute<BasePagedData<ProfileResponseDto>> {
            get(GET_REGISTRATION_REQUESTS_ENDPOINT) {
                if (role != null) parameter("role", role.name)
                if (!search.isNullOrBlank()) parameter("search", search)
                parameter("page", page)
                parameter("size", size)
                parameter("sort", "$sortBy,$sortOrder")
            }
        }
        return response.toPagedData { it.toDomain() }
    }

    override suspend fun approveUser(userId: String) {
        tryToExecute<Unit> {
            post("api/v1/users/$userId/approve")
        }
    }

    override suspend fun rejectUser(userId: String, reason: String) {
        tryToExecute<Unit> {
            post("api/v1/users/$userId/reject") {
                setBody(mapOf("reason" to reason))
            }
        }
    }

    companion object {
        const val GET_ME_ENDPOINT = "api/v1/identity/auth/me"
        const val GET_REGISTRATION_REQUESTS_ENDPOINT = "api/v1/users/status/PENDING_APPROVAL"
    }
}
