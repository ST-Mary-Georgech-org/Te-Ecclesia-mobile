package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("username")
    val username: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("password")
    val password: String
)
