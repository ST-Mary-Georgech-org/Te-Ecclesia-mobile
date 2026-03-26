package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.OtpRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.VerifyOtpRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.BaseResponse
import com.teEcclesia.identity.data.mapper.normalizeEgyptPhone
import com.teEcclesia.identity.data.mapper.toDomain
import com.teEcclesia.identity.data.mapper.toDto
import com.teEcclesia.identity.data.shared.BaseGateway
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
import com.teEcclesia.identity.domain.model.RegisterRequest
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RegisterRepositoryImpl(
    client: HttpClient,
    private val authenticationRepository: AuthenticationRepository
) : BaseGateway(client), RegisterRepository {

    override suspend fun reSendOTP(phone: String) {
        tryToExecute<Unit> {
            post(REGISTER_REQUEST_OTP) {
                setBody(OtpRequestDto(phone.normalizeEgyptPhone()))
            }
        }
    }

    override suspend fun verifyOTPCode(phone: String, otp: String) {
        val response = tryToExecute<BaseResponse<AuthenticationResponse>> {
            post(REGISTER_VERIFY_OTP) {
                setBody(VerifyOtpRequestDto(phone = phone.normalizeEgyptPhone(), otp = otp))
            }
        }.data ?: throw Exception("Invalid OTP")
        authenticationRepository.saveAuthTokens(response.toDomain())
        client.invalidateAuthTokens()
    }

    override suspend fun register(request: RegisterRequest) {
        tryToExecute<Unit> {
            post(REGISTER) {
                setBody(request.toDto())
            }
        }
    }

    companion object {
        const val REGISTER = "api/v1/auth/signup"
        const val REGISTER_REQUEST_OTP = "api/v1/auth/resend-otp"
        const val REGISTER_VERIFY_OTP = "api/v1/auth/verify-signup-otp"
    }
}