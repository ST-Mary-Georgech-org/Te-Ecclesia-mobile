package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.shared.domain.utils.getNow
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceEventDto(
    @SerialName("id")
    val id: Long,
    @SerialName("serviceId")
    val serviceId: Long,
    @SerialName("name")
    val name: String? = null,
    @SerialName("eventDate")
    val eventDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("createdAt")
    val createdAt: String? = null
)

fun ServiceEventDto.toDomain(): ServiceEvent {
    return ServiceEvent(
        id = id,
        serviceId = serviceId,
        name = name,
        eventDate = eventDate.toLocalDateTimeOrDefault().date,
        startTime = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime(0, 0)),
        endTime = runCatching { LocalTime.parse(endTime) }.getOrDefault(LocalTime(0, 0)),
        createdAt = createdAt?.toLocalDateTimeOrDefault() ?: getNow()
    )
}
