package com.teEcclesia.identity.domain.model

data class RegisterRequest(
    val name: String,
    val username: String,
    val password: String,
    val phone: String
)