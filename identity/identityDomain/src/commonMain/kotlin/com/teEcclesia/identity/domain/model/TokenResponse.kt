package com.teEcclesia.identity.domain.model

data class TokenResponse(
    val token: String,
    val refreshToken: String? = null
)
