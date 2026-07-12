package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.OtpRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.ResetPasswordRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.VerifyOtpRequestDto
import com.teEcclesia.identity.data.mapper.normalizeEgyptPhone
import com.teEcclesia.identity.data.shared.BaseGateway
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ResetPasswordRepositoryImpl(
    client: HttpClient
) : BaseGateway(client), ResetPasswordRepository {

    override suspend fun requestOTP(phone: String) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD_REQUEST_OTP) {
                setBody(OtpRequestDto(phone.normalizeEgyptPhone()))
            }
        }
    }

    override suspend fun verifyOTPCode(phone: String, otp: String) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD_VERIFY_OTP) {
                setBody(VerifyOtpRequestDto(phone.normalizeEgyptPhone(), otp))
            }
        }
    }

    override suspend fun resetPassword(phone: String, otp: String, newPassword: String) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD) {
                setBody(ResetPasswordRequestDto(phone.normalizeEgyptPhone(), otp, newPassword))
            }
        }
    }

    override suspend fun reSendOtp(phone: String) {
        tryToExecute<Unit> {
            post(RESET_PASSWORD_RESEND_OTP) {
                setBody(OtpRequestDto(phone.normalizeEgyptPhone()))
            }
        }
    }

    companion object {
        const val RESET_PASSWORD_REQUEST_OTP = "api/v1/auth/forgot-password"
        const val RESET_PASSWORD_VERIFY_OTP = "api/v1/auth/verify-reset-password-otp"
        const val RESET_PASSWORD = "api/v1/auth/reset-password"
        const val RESET_PASSWORD_RESEND_OTP = "api/v1/auth/resend-otp"
    }
}
