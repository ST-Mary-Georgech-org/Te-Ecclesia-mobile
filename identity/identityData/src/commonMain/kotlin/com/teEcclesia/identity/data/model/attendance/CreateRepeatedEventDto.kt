package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ServiceRepeatedEventRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRepeatedEventDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("repeatEvery")
    val repeatEvery: Int
)

fun ServiceRepeatedEventRequest.toCreateRepeatedEventDto() = CreateRepeatedEventDto(
    name = name ?: "",
    startDate = startDate.toString(),
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    repeatEvery = repeatEvery
)