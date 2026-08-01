package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AttendanceRepository {
    suspend fun getServices(): List<ChurchService>
    suspend fun createService(name: String): ChurchService
    suspend fun updateService(id: Long, name: String): ChurchService
    suspend fun deleteService(id: Long)

    suspend fun getEvents(serviceId: Long): List<ServiceEvent>
    suspend fun createEvent(
        serviceId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent

    suspend fun updateEvent(
        eventId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent

    suspend fun deleteEvent(eventId: Long)

    suspend fun getAttendees(eventId: Long): List<EventAttendee>
    suspend fun getUserByCode(code: String): AttendeeUserPreview
    suspend fun addAttendee(eventId: Long, code: String): EventAttendee
    suspend fun removeAttendee(eventId: Long, userId: String)
}
