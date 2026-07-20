package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.InitiateWhatsAppVerificationResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateWhatsAppVerificationResponseDto(
    @SerialName("deepLink")
    val deepLink: String,
    @SerialName("token")
    val token: String
)

fun InitiateWhatsAppVerificationResponseDto.toDomain() = InitiateWhatsAppVerificationResponse(
    deepLink = deepLink,
    token = token
)
