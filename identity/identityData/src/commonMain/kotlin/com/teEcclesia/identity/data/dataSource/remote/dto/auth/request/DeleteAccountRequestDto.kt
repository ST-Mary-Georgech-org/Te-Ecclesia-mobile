package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequestDto(
    @SerialName("reason")
    val reason: String,
    @SerialName("password")
    val password: String
)
