package com.teEcclesia.identity.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAttendeeDto(
    @SerialName("code")
    val code: String
)
