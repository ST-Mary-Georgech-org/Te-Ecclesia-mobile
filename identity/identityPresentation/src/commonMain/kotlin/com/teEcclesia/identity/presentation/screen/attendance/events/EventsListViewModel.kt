package com.teEcclesia.identity.presentation.screen.attendance.events

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.AttendanceRegisterRoute
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.shared.domain.utils.formatTime
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_delete_event
import teecclesia.designsystem.generated.resources.failed_to_load_events
import teecclesia.designsystem.generated.resources.failed_to_save_event

class EventsListViewModel(
    serviceId: Long,
    serviceName: String,
    isResponsible: Boolean,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<EventsListUiState>(EventsListUiState(serviceId = serviceId, serviceName = serviceName, isResponsible = isResponsible)), EventsListInteractionListener {

    private val pageSize = 20

    private val eventsPaginator = createPaginator(
        loadPage = { page ->
            attendanceRepository.getEvents(
                serviceId = state.value.serviceId,
                page = page,
                size = pageSize
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    events = current.events + items.data,
                    totalEvents = items.totalItems,
                    isLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.events.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onReset = {
            updateState { it.copy(events = emptyList(), isLastPage = false) }
        },
        onError = { throwable ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false
                )
            }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_events),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        loadEvents()
    }

    private fun loadEvents() {
        eventsPaginator.reset()
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadEvents()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            eventsPaginator.loadNextItems()
        }
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onClickAddEvent() {
        val now = getNow()
        val startTime = now.time
        val endTime = LocalTime((now.hour + 1) % 24, now.minute)
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingEvent = null,
                eventNameInput = "",
                eventDateInput = now.date.toString(),
                startTimeInput = startTime.formatTime(),
                endTimeInput = endTime.formatTime(),
                isDatePickerOpen = false,
                isStartTimePickerOpen = false,
                isEndTimePickerOpen = false
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
                startTimeInput = event.startTime.formatTime(),
                endTimeInput = event.endTime.formatTime(),
                isDatePickerOpen = false,
                isStartTimePickerOpen = false,
                isEndTimePickerOpen = false
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

    override fun onClickDatePicker() {
        updateState { copy(isDatePickerOpen = true) }
    }

    override fun onDismissDatePicker() {
        updateState { copy(isDatePickerOpen = false) }
    }

    override fun onDateSelected(date: LocalDate) {
        updateState { copy(eventDateInput = date.toString(), isDatePickerOpen = false) }
    }

    override fun onClickStartTimePicker() {
        updateState { copy(isStartTimePickerOpen = true) }
    }

    override fun onDismissStartTimePicker() {
        updateState { copy(isStartTimePickerOpen = false) }
    }

    override fun onStartTimeSelected(time: LocalTime) {
        updateState { copy(startTimeInput = time.formatTime(), isStartTimePickerOpen = false) }
    }

    override fun onClickEndTimePicker() {
        updateState { copy(isEndTimePickerOpen = true) }
    }

    override fun onDismissEndTimePicker() {
        updateState { copy(isEndTimePickerOpen = false) }
    }

    override fun onEndTimeSelected(time: LocalTime) {
        updateState { copy(endTimeInput = time.formatTime(), isEndTimePickerOpen = false) }
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
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_save_event),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
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
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_delete_event),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
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
                isDatePickerOpen = false,
                isStartTimePickerOpen = false,
                isEndTimePickerOpen = false,
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
                eventName = event.name ?: state.value.serviceName,
                isResponsible = state.value.isResponsible
            )
        )
    }
}
