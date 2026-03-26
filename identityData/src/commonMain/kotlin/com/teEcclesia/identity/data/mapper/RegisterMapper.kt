package com.teEcclesia.identity.data.mapper

import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.RegisterRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.domain.model.AuthenticationTokens
import com.teEcclesia.identity.domain.model.RegisterRequest

fun RegisterRequest.toDto() = RegisterRequestDto(
    name = name.trim(),
    username = username.trim(),
    phone = phone.normalizeEgyptPhone(),
    password = password.trim()
)

fun AuthenticationResponse.toDomain() = AuthenticationTokens(
    accessToken = accessToken,
    refreshToken = refreshToken
)