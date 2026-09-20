package com.teEcclesia.identity.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRepeatedEventDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("nextCreationDate")
    val nextCreationDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("repeatEvery")
    val repeatEvery: Int
)
