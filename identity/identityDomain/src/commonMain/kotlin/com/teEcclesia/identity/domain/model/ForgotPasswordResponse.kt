package com.teEcclesia.identity.domain.model

data class ForgotPasswordResponse(
    val link: String,
    val token: String
)
