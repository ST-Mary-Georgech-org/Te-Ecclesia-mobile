package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.ProfileResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.identity.data.utils.getContentTypeAndFilename
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.repository.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.parameter
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.domain.utils.PagedData

import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.identity.domain.model.ApproveUserRequest
import com.teEcclesia.identity.domain.model.RegisterRequest

import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.service.AuthorizationService

import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toCachedProfile

class ProfileRepositoryImpl(
    client: HttpClient,
    private val settingsRepository: SettingsRepository,
    private val authorizationService: AuthorizationService
) : BaseRepository(client), ProfileRepository {

    override suspend fun getRegistrationProfile(): ProfileResponse {
        val response = tryToExecute<ProfileResponseDto> {
            get(GET_ME_ENDPOINT)
        }
        val domainCached = response.toCachedProfile()
        settingsRepository.saveCachedProfile(domainCached)

        val domainProfile = response.toDomain()
        val wasRegistrationPending = authorizationService.isRegistrationPending()
        saveUserAuthorizationDetails(domainProfile)

        if (wasRegistrationPending && domainProfile.status == UserStatus.APPROVED) {
            try {
                authorizationService.upgradeRegistrationToken()
            } catch (_: Exception) {
            }
        }

        return domainProfile
    }

    private suspend fun saveUserAuthorizationDetails(profile: ProfileResponse) {
        saveUserRole(profile.role)
        saveUserStatus(profile.status)
        saveCanApproveRequests(profile.khademProfile?.canApproveRequests ?: false)
        authorizationService.saveKhademAuthorizationDetails(
            stageId = profile.khademProfile?.educationalStage?.id,
            yearId = profile.khademProfile?.educationalYear?.id,
            responsibleStageIds = profile.khademProfile?.responsibleStages?.map { it.id } ?: emptyList(),
            responsibleYearIds = profile.khademProfile?.responsibleYears?.map { it.id } ?: emptyList()
        )
    }

    private suspend fun saveUserRole(role: UserRole) {
        authorizationService.saveUserRole(role)
    }

    private suspend fun saveUserStatus(status: UserStatus) {
        authorizationService.saveUserStatus(status)
    }

    private suspend fun saveCanApproveRequests(canApprove: Boolean) {
        authorizationService.saveCanApproveRequests(canApprove)
    }

    override suspend fun getUserProfile(userId: String): ProfileResponse {
        val response = tryToExecute<ProfileResponseDto> {
            get("api/v1/users/$userId")
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

    override suspend fun approveUser(userId: String, request: ApproveUserRequest?) {
        tryToExecute<Unit> {
            post("api/v1/users/$userId/approve") {
                request?.let { setBody(it.toDto()) }
            }
        }
    }

    override suspend fun rejectUser(userId: String, reason: String) {
        tryToExecute<Unit> {
            post("api/v1/users/$userId/reject") {
                setBody(mapOf("reason" to reason))
            }
        }
    }

    override suspend fun createMakhdoomDirectly(
        request: RegisterRequest,
        imageBytes: ByteArray?,
        identityDocumentBytes: ByteArray?
    ): ProfileResponse {
        val requestJson = Json.encodeToString(request.toDto())
        val response = tryToExecute<ProfileResponseDto> {
            post("api/v1/users/makhdoom") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("request", requestJson, Headers.build {
                                append(HttpHeaders.ContentType, "application/json")
                            })
                            if (imageBytes != null) {
                                val (contentType, filename) = getContentTypeAndFilename(imageBytes, "image")
                                append("image", imageBytes, Headers.build {
                                    append(HttpHeaders.ContentType, contentType)
                                    append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                                })
                            }
                            if (identityDocumentBytes != null) {
                                val (contentType, filename) = getContentTypeAndFilename(identityDocumentBytes, "identityDocument")
                                append("identityDocument", identityDocumentBytes, Headers.build {
                                    append(HttpHeaders.ContentType, contentType)
                                    append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                                })
                            }
                        }
                    )
                )
            }
        }
        return response.toDomain()
    }

    override suspend fun getApprovedUsers(
        search: String?,
        stageId: Long?,
        yearId: Long?,
        role: UserRole?,
        page: Int,
        size: Int,
        sortBy: String,
        sortOrder: String
    ): PagedData<ProfileResponse> {
        val response = tryToExecute<BasePagedData<ProfileResponseDto>> {
            get("api/v1/users/status/APPROVED") {
                if (!search.isNullOrBlank()) parameter("search", search)
                if (stageId != null) parameter("stageId", stageId)
                if (yearId != null) parameter("yearId", yearId)
                if (role != null) parameter("role", role.name)
                parameter("page", page)
                parameter("size", size)
                parameter("sort", "$sortBy,$sortOrder")
            }
        }
        return response.toPagedData { it.toDomain() }
    }

    companion object {
        const val GET_ME_ENDPOINT = "api/v1/identity/auth/me"
        const val GET_REGISTRATION_REQUESTS_ENDPOINT = "api/v1/users/status/PENDING_APPROVAL"
    }
}
