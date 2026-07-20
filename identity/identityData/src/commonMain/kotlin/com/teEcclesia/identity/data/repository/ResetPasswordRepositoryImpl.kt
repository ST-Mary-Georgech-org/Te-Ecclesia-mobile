package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.ForgotPasswordRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.ResetPasswordRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.VerifyOtpRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.ForgotPasswordResponseDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.shared.data.shared.BaseGateway
import com.teEcclesia.identity.domain.model.ForgotPasswordResponse
import com.teEcclesia.identity.domain.model.VerificationMethod
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ResetPasswordRepositoryImpl(
    client: HttpClient
) : BaseGateway(client), ResetPasswordRepository {

    override suspend fun requestOTP(key: String, method: VerificationMethod): ForgotPasswordResponse? {
        val response = tryToExecute<ForgotPasswordResponseDto> {
            post(RESET_PASSWORD_REQUEST_OTP) {
                setBody(ForgotPasswordRequestDto(key = key, method = method))
            }
        }
        return response?.toDomain()
    }

    override suspend fun verifyOTPCode(key: String, otp: String, method: VerificationMethod, deviceToken: String?) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD_VERIFY_OTP) {
                setBody(VerifyOtpRequestDto(key = key, otp = otp, method = method, deviceToken = deviceToken))
            }
        }
    }

    override suspend fun resetPassword(key: String, otp: String, newPassword: String, method: VerificationMethod) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD) {
                setBody(ResetPasswordRequestDto(key = key, otp = otp, newPassword = newPassword, method = method))
            }
        }
    }

    override suspend fun reSendOtp(key: String, method: VerificationMethod): ForgotPasswordResponse? {
        val response = tryToExecute<ForgotPasswordResponseDto> {
            post(RESET_PASSWORD_RESEND_OTP) {
                setBody(ForgotPasswordRequestDto(key = key, method = method))
            }
        }
        return response?.toDomain()
    }

    companion object {
        const val RESET_PASSWORD_REQUEST_OTP = "api/v1/identity/auth/forgot-password"
        const val RESET_PASSWORD_VERIFY_OTP = "api/v1/identity/auth/verify-otp"
        const val RESET_PASSWORD = "api/v1/identity/auth/reset-password"
        const val RESET_PASSWORD_RESEND_OTP = "api/v1/identity/auth/resend-otp"
    }
}
