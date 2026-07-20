package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceTokenRequestDto(
    val refreshToken: String,
    val deviceToken: String
)
