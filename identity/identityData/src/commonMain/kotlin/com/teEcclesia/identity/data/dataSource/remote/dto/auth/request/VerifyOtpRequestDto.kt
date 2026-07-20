package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.VerificationMethod

@Serializable
data class VerifyOtpRequestDto(
    @SerialName("key")
    val key: String,
    @SerialName("otp")
    val otp: String,
    @SerialName("method")
    val method: VerificationMethod,
    @SerialName("deviceToken")
    val deviceToken: String? = null
)
