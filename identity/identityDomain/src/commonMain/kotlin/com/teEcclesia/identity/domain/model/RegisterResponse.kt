package com.teEcclesia.identity.domain.model

data class RegisterResponse(
    val message: String,
    val whatsappDeepLink: String,
    val token: String
)
