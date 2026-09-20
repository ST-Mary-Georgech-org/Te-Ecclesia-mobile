package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ServiceRepeatedEvent
import com.teEcclesia.shared.domain.utils.getNow
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceRepeatedEventDto(
    @SerialName("id")
    val id: Long,
    @SerialName("serviceId")
    val serviceId: Long,
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
    val repeatEvery: Int? = 0,
    @SerialName("createdAt")
    val createdAt: String? = null
)

fun ServiceRepeatedEventDto.toDomain(): ServiceRepeatedEvent {
    return ServiceRepeatedEvent(
        id = id,
        serviceId = serviceId,
        name = name,
        startDate = startDate.toLocalDateTimeOrDefault().date,
        nextCreationDate = nextCreationDate.toLocalDateTimeOrDefault().date,
        startTime = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime(0, 0)),
        endTime = runCatching { LocalTime.parse(endTime) }.getOrDefault(LocalTime(0, 0)),
        repeatEvery = repeatEvery ?: 0,
        createdAt = createdAt?.toLocalDateTimeOrDefault() ?: getNow()
    )
}
