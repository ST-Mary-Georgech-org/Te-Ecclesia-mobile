package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.domain.model.LoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("identifier")
    val identifier: String,
    @SerialName("password")
    val password: String,
    @SerialName("deviceToken")
    val deviceToken: String? = null
)

fun LoginRequest.toDto() = LoginRequestDto(
    identifier = identifier,
    password = password,
    deviceToken = deviceToken
)
