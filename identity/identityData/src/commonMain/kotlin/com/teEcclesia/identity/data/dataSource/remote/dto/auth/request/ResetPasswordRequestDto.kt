package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    @SerialName("phone")
    val phone: String,
    @SerialName("otp")
    val otp: String,
    @SerialName("newPassword")
    val newPassword: String
)
