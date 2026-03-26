package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class VerifyOtpRequestDto (
    @SerialName("phone")
    val phone: String,
    @SerialName("otp")
    val otp: String,
)