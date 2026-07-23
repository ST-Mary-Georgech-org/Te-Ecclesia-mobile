package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.TokenResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto(
    @SerialName("token")
    val token: String,
    @SerialName("refreshToken")
    val refreshToken: String? = null
)

fun TokenResponseDto.toDomain() = TokenResponse(
    token = token,
    refreshToken = refreshToken
)
