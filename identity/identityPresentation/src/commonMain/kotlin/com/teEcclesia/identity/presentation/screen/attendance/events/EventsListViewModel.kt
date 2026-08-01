package com.teEcclesia.identity.presentation.screen.attendance.events

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.AttendanceRegisterRoute
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.shared.domain.utils.getToday
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class EventsListViewModel(
    serviceId: Long,
    serviceName: String,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<EventsListUiState>(EventsListUiState(serviceId = serviceId, serviceName = serviceName)), EventsListInteractionListener {

    init {
        loadEvents()
    }

    private fun loadEvents() {
        tryToCall(
            onStart = { updateState { copy(isLoading = true) } },
            block = { attendanceRepository.getEvents(state.value.serviceId) },
            onSuccess = { events ->
                updateState { copy(events = events) }
            },
            onError = { },
            onEnd = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onClickAddEvent() {
        val today = getToday()
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingEvent = null,
                eventNameInput = "",
                eventDateInput = today.toString(),
                startTimeInput = "18:00",
                endTimeInput = "20:00"
            )
        }
    }

    override fun onClickEditEvent(event: ServiceEvent) {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingEvent = event,
                eventNameInput = event.name ?: "",
                eventDateInput = event.eventDate.toString(),
                startTimeInput = event.startTime.toString(),
                endTimeInput = event.endTime.toString()
            )
        }
    }

    override fun onClickDeleteEvent(event: ServiceEvent) {
        updateState {
            copy(
                isDeleteConfirmSheetOpen = true,
                deletingEvent = event
            )
        }
    }

    override fun onEventNameChanged(name: String) {
        updateState { copy(eventNameInput = name) }
    }

    override fun onEventDateChanged(date: String) {
        updateState { copy(eventDateInput = date) }
    }

    override fun onStartTimeChanged(time: String) {
        updateState { copy(startTimeInput = time) }
    }

    override fun onEndTimeChanged(time: String) {
        updateState { copy(endTimeInput = time) }
    }

    override fun onConfirmSaveEvent() {
        val dateStr = state.value.eventDateInput.trim()
        val start = state.value.startTimeInput.trim()
        val end = state.value.endTimeInput.trim()
        val date = runCatching { LocalDate.parse(dateStr) }.getOrNull() ?: return
        val startTime = runCatching { LocalTime.parse(start) }.getOrNull() ?: return
        val endTime = runCatching { LocalTime.parse(end) }.getOrNull() ?: return

        val editing = state.value.editingEvent
        val name = state.value.eventNameInput.trim()

        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = {
                if (editing == null) {
                    attendanceRepository.createEvent(
                        serviceId = state.value.serviceId,
                        name = name.ifBlank { null },
                        date = date,
                        startTime = startTime,
                        endTime = endTime
                    )
                } else {
                    attendanceRepository.updateEvent(
                        eventId = editing.id,
                        name = name.ifBlank { null },
                        date = date,
                        startTime = startTime,
                        endTime = endTime
                    )
                }
            },
            onSuccess = {
                onDismissSheet()
                loadEvents()
            },
            onError = { },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onConfirmDeleteEvent() {
        val event = state.value.deletingEvent ?: return
        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = { attendanceRepository.deleteEvent(event.id) },
            onSuccess = {
                onDismissSheet()
                loadEvents()
            },
            onError = { },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onDismissSheet() {
        updateState {
            copy(
                isAddEditSheetOpen = false,
                editingEvent = null,
                eventNameInput = "",
                eventDateInput = "",
                startTimeInput = "",
                endTimeInput = "",
                isDeleteConfirmSheetOpen = false,
                deletingEvent = null
            )
        }
    }

    override fun onClickEvent(event: ServiceEvent) {
        navigate(
            AttendanceRegisterRoute(
                eventId = event.id,
                serviceName = state.value.serviceName,
                eventName = event.name ?: state.value.serviceName
            )
        )
    }
}
