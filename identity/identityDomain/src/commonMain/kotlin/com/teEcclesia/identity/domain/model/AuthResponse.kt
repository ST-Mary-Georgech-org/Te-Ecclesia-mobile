package com.teEcclesia.identity.domain.model

data class AuthResponse(
    val token: String,
    val profile: ProfileResponse
)
