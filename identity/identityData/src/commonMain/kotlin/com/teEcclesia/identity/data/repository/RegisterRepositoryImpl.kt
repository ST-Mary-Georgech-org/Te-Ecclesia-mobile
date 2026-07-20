package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.VerifyEmailRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.InitiateWhatsAppVerificationResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.RegisterResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.TokenResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.shared.data.shared.BaseGateway
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
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

class RegisterRepositoryImpl(
    client: HttpClient,
    private val authenticationRepository: AuthenticationRepository
) : BaseGateway(client), RegisterRepository {

    override suspend fun register(
        request: RegisterRequest,
        imageBytes: ByteArray?,
        certificateImageBytes: ByteArray?
    ): TokenResponse {
        val requestJson = Json.encodeToString(request.toDto())
        
        val response = tryToExecute<TokenResponseDto> {
            post(REGISTER) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("request", requestJson, Headers.build {
                                append(HttpHeaders.ContentType, "application/json")
                            })
                            if (imageBytes != null) {
                                append("image", imageBytes, Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                                })
                            }
                            if (certificateImageBytes != null) {
                                append("certificateImage", certificateImageBytes, Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=\"certificate.jpg\"")
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
        certificateImageBytes: ByteArray?
    ): RegisterResponse {
        val requestJson = Json.encodeToString(request.toDto())
        
        val response = tryToExecute<RegisterResponseDto> {
            post(COMPLETE_PROFILE) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("request", requestJson, Headers.build {
                                append(HttpHeaders.ContentType, "application/json")
                            })
                            if (certificateImageBytes != null) {
                                append("certificateImage", certificateImageBytes, Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=\"certificate.jpg\"")
                                })
                            }
                        }
                    )
                )
            }
        }
        
        return response.toDomain()
    }

    override suspend fun verifyEmail(email: String, otp: String, deviceToken: String?) {
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
        } ?: throw Exception("Verification pending or failed")
        
        authenticationRepository.saveAuthTokens(response.toDomain())
        client.invalidateAuthTokens()
    }

    companion object {
        const val REGISTER = "api/v1/identity/auth/signup"
        const val COMPLETE_PROFILE = "api/v1/identity/auth/complete-profile"
        const val VERIFY_EMAIL = "api/v1/identity/auth/verify-email"
        const val WHATSAPP_INITIATE = "api/v1/identity/auth/whatsapp/initiate"
        const val WHATSAPP_STATUS = "api/v1/identity/auth/whatsapp/status"
    }
}
