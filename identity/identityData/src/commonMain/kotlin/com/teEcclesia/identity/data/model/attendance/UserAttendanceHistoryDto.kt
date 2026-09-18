package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.UserAttendanceHistory
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAttendanceHistoryDto(
    @SerialName("id")
    val id: Long,
    @SerialName("eventId")
    val eventId: Long,
    @SerialName("serviceId")
    val serviceId: Long,
    @SerialName("serviceName")
    val serviceName: String,
    @SerialName("eventName")
    val eventName: String? = null,
    @SerialName("eventDate")
    val eventDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("registeredAt")
    val registeredAt: String
)

fun UserAttendanceHistoryDto.toDomain(): UserAttendanceHistory {
    return UserAttendanceHistory(
        id = id,
        eventId = eventId,
        serviceId = serviceId,
        serviceName = serviceName,
        eventName = eventName,
        eventDate = eventDate.toLocalDateTimeOrDefault().date,
        startTime = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime(0, 0)),
        endTime = runCatching { LocalTime.parse(endTime) }.getOrDefault(LocalTime(0, 0)),
        registeredAt = registeredAt.toLocalDateTimeOrDefault()
    )
}
