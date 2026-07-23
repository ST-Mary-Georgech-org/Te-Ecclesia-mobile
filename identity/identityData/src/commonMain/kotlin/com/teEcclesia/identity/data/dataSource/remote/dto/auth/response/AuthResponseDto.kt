package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.AuthResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    @SerialName("token")
    val token: String,
    @SerialName("profile")
    val profile: ProfileResponseDto
)

fun AuthResponseDto.toDomain() = AuthResponse(
    token = token,
    profile = profile.toDomain()
)
