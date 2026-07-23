package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.ForgotPasswordResponse
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponseDto(
    val link: String,
    val token: String
)

fun ForgotPasswordResponseDto.toDomain() = ForgotPasswordResponse(
    link = link,
    token = token
)
