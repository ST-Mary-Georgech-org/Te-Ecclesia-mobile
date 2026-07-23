package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.RegisterResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponseDto(
    @SerialName("message")
    val message: String,
    @SerialName("whatsappDeepLink")
    val whatsappDeepLink: String,
    @SerialName("token")
    val token: String
)

fun RegisterResponseDto.toDomain() = RegisterResponse(
    message = message,
    whatsappDeepLink = whatsappDeepLink,
    token = token
)
