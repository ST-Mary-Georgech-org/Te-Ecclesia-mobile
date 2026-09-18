package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactivateAccountRequestDto(
    @SerialName("nationalId")
    val nationalId: String,
    @SerialName("password")
    val password: String,
    @SerialName("deviceToken")
    val deviceToken: String? = null
)
