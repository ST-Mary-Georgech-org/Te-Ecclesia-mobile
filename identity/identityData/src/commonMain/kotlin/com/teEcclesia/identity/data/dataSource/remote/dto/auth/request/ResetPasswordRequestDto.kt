package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.VerificationMethod

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("key")
    val key: String,
    @SerialName("otp")
    val otp: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("method")
    val method: VerificationMethod
)
