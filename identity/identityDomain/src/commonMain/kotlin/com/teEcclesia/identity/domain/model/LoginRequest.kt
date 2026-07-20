package com.teEcclesia.identity.domain.model

data class LoginRequest(
    val identifier: String,
    val password: String,
    val deviceToken: String? = null
)
