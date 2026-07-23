package com.teEcclesia.shared.data.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String
)
