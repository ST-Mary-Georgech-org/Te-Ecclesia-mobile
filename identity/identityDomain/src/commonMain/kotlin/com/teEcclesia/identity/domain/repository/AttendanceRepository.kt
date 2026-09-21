package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.identity.domain.model.attendance.ServiceRepeatedEvent
import com.teEcclesia.identity.domain.model.attendance.ServiceRepeatedEventRequest
import com.teEcclesia.identity.domain.model.attendance.UserAttendanceHistory
import com.teEcclesia.shared.domain.utils.PagedData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface AttendanceRepository {
    suspend fun getServices(page: Int, size: Int = 20): PagedData<ChurchService>
    suspend fun createService(
        name: String,
        educationalStageIds: List<Long>,
        responsibleServantIds: List<String>,
        repeatedEvent : ServiceRepeatedEventRequest?
    ): ChurchService
    suspend fun updateService(
        id: Long,
        name: String,
        educationalStageIds: List<Long>,
        responsibleServantIds: List<String>,
        repeatedEvent : ServiceRepeatedEventRequest?
    ): ChurchService
    suspend fun deleteService(id: Long)

    suspend fun getEvents(serviceId: Long, page: Int, size: Int = 20): PagedData<ServiceEvent>
    suspend fun createEvent(
        serviceId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent

    suspend fun createRepeatedEvent(
        serviceId: Long,
        name: String?,
        startDate: LocalDate,
        nextCreationDate: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        repeatEvery : Int
    ): ServiceRepeatedEvent

    suspend fun updateEvent(
        eventId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent

    suspend fun deleteEvent(eventId: Long)

    suspend fun getAttendees(eventId: Long, page: Int, size: Int = 20): PagedData<EventAttendee>
    suspend fun getUserByCode(code: String): AttendeeUserPreview
    suspend fun searchUsers(query: String): List<AttendeeUserPreview>
    suspend fun searchServants(query: String): List<AttendeeUserPreview>
    suspend fun addAttendee(eventId: Long, code: String): EventAttendee
    suspend fun removeAttendee(eventId: Long, userId: String)
    suspend fun getUserAttendanceHistory(userId: String, serviceId: Long? = null, page: Int, size: Int = 20): PagedData<UserAttendanceHistory>
}

