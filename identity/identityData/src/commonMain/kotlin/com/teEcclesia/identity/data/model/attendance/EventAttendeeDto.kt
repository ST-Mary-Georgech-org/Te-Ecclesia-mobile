package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventAttendeeDto(
    @SerialName("id")
    val id: Long,
    @SerialName("eventId")
    val eventId: Long,
    @SerialName("userId")
    val userId: String,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: String,
    @SerialName("stageName")
    val stageName: String? = null,
    @SerialName("yearName")
    val yearName: String? = null,
    @SerialName("registeredAt")
    val registeredAt: String
)

fun EventAttendeeDto.toDomain(): EventAttendee {
    return EventAttendee(
        id = id,
        eventId = eventId,
        userId = userId,
        name = name,
        role = runCatching { UserRole.valueOf(role) }.getOrDefault(UserRole.MAKHDOOM),
        stageName = stageName,
        yearName = yearName,
        registeredAt = registeredAt.toLocalDateTimeOrDefault()
    )
}
