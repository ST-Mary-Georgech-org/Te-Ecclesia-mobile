package com.teEcclesia.shared.data.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncompleteProfileResponse(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("token")
    val token: String? = null,
    @SerialName("refreshToken")
    val refreshToken: String? = null
)