package com.teEcclesia.identity.domain.model

data class AuthenticationTokens(
    val accessToken: String,
    val refreshToken: String
)
