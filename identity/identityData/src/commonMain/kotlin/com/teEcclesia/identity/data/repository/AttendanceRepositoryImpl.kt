package com.teEcclesia.identity.data.repository

import com.teEcclesia.identity.data.model.attendance.AddAttendeeDto
import com.teEcclesia.identity.data.model.attendance.AttendeeUserPreviewDto
import com.teEcclesia.identity.data.model.attendance.ChurchServiceDto
import com.teEcclesia.identity.data.model.attendance.CreateEventDto
import com.teEcclesia.identity.data.model.attendance.CreateServiceDto
import com.teEcclesia.identity.data.model.attendance.EventAttendeeDto
import com.teEcclesia.identity.data.model.attendance.ServiceEventDto
import com.teEcclesia.identity.data.model.attendance.toDomain
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.shared.data.shared.BaseRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class AttendanceRepositoryImpl(
    client: HttpClient
) : BaseRepository(client), AttendanceRepository {

    override suspend fun getServices(): List<ChurchService> {
        val dtos = tryToExecute<List<ChurchServiceDto>> {
            get("/api/v1/attendance/services")
        }
        return dtos.map { it.toDomain() }
    }

    override suspend fun createService(name: String): ChurchService {
        val dto = tryToExecute<ChurchServiceDto> {
            post("/api/v1/attendance/services") {
                contentType(ContentType.Application.Json)
                setBody(CreateServiceDto(name = name))
            }
        }
        return dto.toDomain()
    }

    override suspend fun updateService(id: Long, name: String): ChurchService {
        val dto = tryToExecute<ChurchServiceDto> {
            put("/api/v1/attendance/services/$id") {
                contentType(ContentType.Application.Json)
                setBody(CreateServiceDto(name = name))
            }
        }
        return dto.toDomain()
    }

    override suspend fun deleteService(id: Long) {
        tryToExecute<Unit> {
            delete("/api/v1/attendance/services/$id")
        }
    }

    override suspend fun getEvents(serviceId: Long): List<ServiceEvent> {
        val dtos = tryToExecute<List<ServiceEventDto>> {
            get("/api/v1/attendance/services/$serviceId/events")
        }
        return dtos.map { it.toDomain() }
    }

    override suspend fun createEvent(
        serviceId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent {
        val dto = tryToExecute<ServiceEventDto> {
            post("/api/v1/attendance/services/$serviceId/events") {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateEventDto(
                        name = name,
                        eventDate = date.toString(),
                        startTime = startTime.toString(),
                        endTime = endTime.toString()
                    )
                )
            }
        }
        return dto.toDomain()
    }

    override suspend fun updateEvent(
        eventId: Long,
        name: String?,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): ServiceEvent {
        val dto = tryToExecute<ServiceEventDto> {
            put("/api/v1/attendance/events/$eventId") {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateEventDto(
                        name = name,
                        eventDate = date.toString(),
                        startTime = startTime.toString(),
                        endTime = endTime.toString()
                    )
                )
            }
        }
        return dto.toDomain()
    }

    override suspend fun deleteEvent(eventId: Long) {
        tryToExecute<Unit> {
            delete("/api/v1/attendance/events/$eventId")
        }
    }

    override suspend fun getAttendees(eventId: Long): List<EventAttendee> {
        val dtos = tryToExecute<List<EventAttendeeDto>> {
            get("/api/v1/attendance/events/$eventId/attendees")
        }
        return dtos.map { it.toDomain() }
    }

    override suspend fun getUserByCode(code: String): AttendeeUserPreview {
        val dto = tryToExecute<AttendeeUserPreviewDto> {
            get("/api/v1/attendance/users/by-code/$code")
        }
        return dto.toDomain()
    }

    override suspend fun addAttendee(eventId: Long, code: String): EventAttendee {
        val dto = tryToExecute<EventAttendeeDto> {
            post("/api/v1/attendance/events/$eventId/attendees") {
                contentType(ContentType.Application.Json)
                setBody(AddAttendeeDto(code = code))
            }
        }
        return dto.toDomain()
    }

    override suspend fun removeAttendee(eventId: Long, userId: String) {
        tryToExecute<Unit> {
            delete("/api/v1/attendance/events/$eventId/attendees/$userId")
        }
    }
}
