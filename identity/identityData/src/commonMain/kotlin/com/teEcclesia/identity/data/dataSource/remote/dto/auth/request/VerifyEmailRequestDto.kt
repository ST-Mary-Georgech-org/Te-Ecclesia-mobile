package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailRequestDto(
    val email: String,
    val otp: String,
    val deviceToken: String? = null
)
