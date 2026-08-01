package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.VerifyEmailRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.InitiateWhatsAppVerificationResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.RegisterResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.TokenResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
import com.teEcclesia.identity.data.utils.getContentTypeAndFilename
import com.teEcclesia.identity.domain.model.CompleteProfileRequest
import com.teEcclesia.identity.domain.model.InitiateWhatsAppVerificationResponse
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.model.RegisterResponse
import com.teEcclesia.identity.domain.model.TokenResponse
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

import com.teEcclesia.identity.data.dto.PriestDto
import com.teEcclesia.identity.data.dto.UserSummaryDto
import com.teEcclesia.identity.data.dto.toDomain
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData

import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.push.firebase.firebasePushNotifier

class RegisterRepositoryImpl(
    client: HttpClient,
    private val authenticationRepository: AuthenticationRepository
) : BaseRepository(client), RegisterRepository {

    override suspend fun register(
        request: RegisterRequest,
        imageBytes: ByteArray?,
        certificateImageBytes: ByteArray?
    ): TokenResponse {
        val deviceToken = runCatching { KMPNotifier.firebasePushNotifier.getToken() }.getOrNull()
        val requestJson = Json.encodeToString(request.toDto(deviceToken))
        
        val response = tryToExecute<TokenResponseDto> {
            post(REGISTER) {
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
                            if (certificateImageBytes != null) {
                                val (contentType, filename) = getContentTypeAndFilename(certificateImageBytes, "certificate")
                                append("certificateImage", certificateImageBytes, Headers.build {
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

    override suspend fun completeProfile(
        request: CompleteProfileRequest,
        ordinationCertificateBytes: ByteArray?,
        identityDocumentBytes: ByteArray?
    ): RegisterResponse {
        val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
        val requestJson = Json.encodeToString(request.toDto(deviceToken))
        
        val response = tryToExecute<RegisterResponseDto> {
            post(COMPLETE_PROFILE) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("request", requestJson, Headers.build {
                                append(HttpHeaders.ContentType, "application/json")
                            })
                            if (ordinationCertificateBytes != null) {
                                val (contentType, filename) = getContentTypeAndFilename(ordinationCertificateBytes, "certificate")
                                append("certificateImage", ordinationCertificateBytes, Headers.build {
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

    override suspend fun verifyEmail(email: String, otp: String) {
        val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
        val response = tryToExecute<AuthenticationResponse> {
            post(VERIFY_EMAIL) {
                setBody(VerifyEmailRequestDto(email = email, otp = otp, deviceToken = deviceToken))
            }
        }
        
        authenticationRepository.saveAuthTokens(response.toDomain())
        client.invalidateAuthTokens()
    }

    override suspend fun initiateWhatsAppVerification(phone: String): InitiateWhatsAppVerificationResponse {
        val response = tryToExecute<InitiateWhatsAppVerificationResponseDto> {
            post(WHATSAPP_INITIATE) {
                parameter("phone", phone)
            }
        }
        
        return response.toDomain()
    }

    override suspend fun getWhatsAppStatus(token: String) {
        val response = tryToExecute<AuthenticationResponse> {
            get(WHATSAPP_STATUS) {
                parameter("token", token)
            }
        }
        
        authenticationRepository.saveAuthTokens(response.toDomain())
        client.invalidateAuthTokens()
    }

    override suspend fun getConfessionPriests(pageQuery: PageQuery): PagedData<Priest> {
        return tryToExecute<BasePagedData<PriestDto>> {
            get(PRIESTS) {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun searchParent(query: String): UserSummary? {
        val dto = tryToExecute<UserSummaryDto?> {
            get(SEARCH_PARENTS) {
                parameter("query", query)
            }
        }
        return dto?.toDomain()
    }

    override suspend fun searchMakhdoom(query: String): UserSummary? {
        val dto = tryToExecute<UserSummaryDto?> {
            get(SEARCH_MAKHDOOMS) {
                parameter("query", query)
            }
        }
        return dto?.toDomain()
    }

    companion object {
        const val REGISTER = "api/v1/identity/auth/signup"
        const val COMPLETE_PROFILE = "api/v1/identity/auth/complete-profile"
        const val VERIFY_EMAIL = "api/v1/identity/auth/verify-email"
        const val WHATSAPP_INITIATE = "api/v1/identity/auth/whatsapp/initiate"
        const val WHATSAPP_STATUS = "api/v1/identity/auth/whatsapp/status"
        const val PRIESTS = "api/v1/identity/auth/priests"
        const val SEARCH_PARENTS = "api/v1/identity/auth/search-parents"
        const val SEARCH_MAKHDOOMS = "api/v1/identity/auth/search-makhdooms"
    }
}