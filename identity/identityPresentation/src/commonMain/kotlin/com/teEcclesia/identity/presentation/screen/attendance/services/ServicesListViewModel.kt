package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.api.AttendanceEventsRoute
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.identity.domain.model.attendance.ServiceRepeatedEventRequest
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.formatTime
import com.teEcclesia.shared.domain.utils.parseDate
import com.teEcclesia.shared.domain.utils.parseTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.end_time_must_be_after_start_time
import teecclesia.designsystem.generated.resources.failed_to_delete_service
import teecclesia.designsystem.generated.resources.failed_to_load_services
import teecclesia.designsystem.generated.resources.failed_to_save_service
import teecclesia.designsystem.generated.resources.field_required
import teecclesia.designsystem.generated.resources.maximum_servants_reached
import teecclesia.designsystem.generated.resources.maximum_stages_reached
import teecclesia.designsystem.generated.resources.repeat_every_must_be_greater_than_zero
import kotlin.time.Duration.Companion.milliseconds

class ServicesListViewModel(
    private val attendanceRepository: AttendanceRepository,
    private val lookupRepository: LookupRepository,
    private val authorizationService: AuthorizationService
) : BaseViewModel<ServicesListUiState>(ServicesListUiState()), ServicesListInteractionListener {

    private val pageSize = 20
    private var servantSearchJob: Job? = null

    private val servicesPaginator = createPaginator(
        loadPage = { page ->
            attendanceRepository.getServices(
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
                    services = current.services + items.data,
                    totalServices = items.totalItems,
                    isLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.services.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onReset = {
            updateState { it.copy(services = emptyList(), isLastPage = false) }
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
                    title = UiText.StringRes(Res.string.failed_to_load_services),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    private val stagesPaginator = createPaginator(
        loadPage = { page ->
            lookupRepository.getEducationalStages(PageQuery(page = page, size = 20)).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    educationalStages = current.educationalStages + items.data,
                    isStageLoadFailed = false,
                    isStageLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.educationalStages.isEmpty()) {
                    current.copy(isStageLoading = loading)
                } else {
                    current.copy(isStagePagingLoading = loading)
                }
            }
        },
        onReset = {
            updateState { it.copy(educationalStages = emptyList(), isStageLoadFailed = false, isStageLastPage = false) }
        },
        onError = { _ ->
            updateState { it.copy(isStageLoading = false, isStagePagingLoading = false, isStageLoadFailed = true) }
        }
    )

    init {
        val isAdmin = authorizationService.getUserRole() == UserRole.ADMIN
        updateState { copy(isAdmin = isAdmin) }
        loadServices()
        loadEducationalStages()
    }

    private fun loadServices() {
        servicesPaginator.reset()
    }

    private fun loadEducationalStages() {
        stagesPaginator.reset()
    }

    override fun onLoadNextStages() {
        if (!state.value.isStageLastPage && !state.value.isStagePagingLoading && !state.value.isStageLoading) {
            stagesPaginator.loadNextItems()
        }
    }

    override fun onRetryLoadStages() {
        stagesPaginator.reset()
    }

    override fun onToggleStageSheet(visible: Boolean) {
        updateState { copy(isStageSheetVisible = visible) }
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadServices()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            servicesPaginator.loadNextItems()
        }
    }

    override fun onClickAddService() {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingService = null,
                serviceNameInput = "",
                serviceNameError = null,
                selectedStages = emptyList(),
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = emptyList(),
                addRepeatedEvent = false,
                eventNameInput = "",
                eventDateInput = "",
                eventDateError = null,
                repeatEvery = "",
                repeatEveryError = null,
                startTimeInput = "",
                startTimeError = null,
                endTimeInput = "",
                endTimeError = null
            )
        }
    }

    override fun onClickEditService(service: ChurchService) {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingService = service,
                serviceNameInput = service.name,
                serviceNameError = null,
                selectedStages = service.educationalStages,
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = service.responsibleServants,
                addRepeatedEvent = service.repeatedEvent != null,
                eventNameInput = service.repeatedEvent?.name ?: "",
                eventDateInput = service.repeatedEvent?.startDate?.toString() ?: "",
                eventDateError = null,
                repeatEvery = service.repeatedEvent?.repeatEvery?.toString() ?: "",
                repeatEveryError = null,
                startTimeInput = service.repeatedEvent?.startTime?.formatTime() ?: "",
                startTimeError = null,
                endTimeInput = service.repeatedEvent?.endTime?.formatTime() ?: "",
                endTimeError = null
            )
        }
    }

    override fun onClickDeleteService(service: ChurchService) {
        updateState {
            copy(
                isDeleteConfirmSheetOpen = true,
                deletingService = service
            )
        }
    }

    override fun onServiceNameChanged(name: String) {
        updateState {
            copy(
                serviceNameInput = name,
                serviceNameError = null
            )
        }
    }

    override fun onToggleStageSelection(stage: LookupResponse) {
        val currentStages = state.value.selectedStages
        val isAlreadySelected = currentStages.any { it.id == stage.id }
        if (isAlreadySelected) {
            updateState { copy(selectedStages = currentStages.filter { it.id != stage.id }) }
        } else {
            if (currentStages.size >= 10) {
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_save_service),
                    message = UiText.StringRes(Res.string.maximum_stages_reached),
                    isSuccess = false
                )
                return
            }
            updateState { copy(selectedStages = currentStages + stage) }
        }
    }

    override fun onServantSearchQueryChanged(query: String) {
        updateState { copy(servantSearchQuery = query) }
        servantSearchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.length >= 2) {
            servantSearchJob = launch {
                delay(300.milliseconds)
                searchServants(trimmed)
            }
        } else {
            updateState {
                copy(
                    suggestedServants = emptyList(),
                    isServantsDropdownVisible = false,
                    isSearchingServants = false
                )
            }
        }
    }

    private fun searchServants(query: String) {
        tryToCall(
            onStart = { updateState { copy(isSearchingServants = true) } },
            block = { attendanceRepository.searchServants(query) },
            onSuccess = { servants ->
                val selectedIds = state.value.selectedServants.map { it.id }.toSet()
                val filtered = servants.filter { it.id !in selectedIds }
                updateState {
                    copy(
                        suggestedServants = filtered,
                        isServantsDropdownVisible = filtered.isNotEmpty()
                    )
                }
            },
            onError = {
                updateState { copy(suggestedServants = emptyList(), isServantsDropdownVisible = false) }
            },
            onEnd = { updateState { copy(isSearchingServants = false) } }
        )
    }

    override fun onSelectServant(servant: AttendeeUserPreview) {
        if (state.value.selectedServants.size >= 30) {
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_save_service),
                message = UiText.StringRes(Res.string.maximum_servants_reached),
                isSuccess = false
            )
            return
        }

        val newResponsible = ResponsibleServant(
            id = servant.id,
            name = servant.name,
            code = servant.code,
            imageUrl = servant.imageUrl
        )
        updateState {
            copy(
                selectedServants = if (selectedServants.none { it.id == servant.id }) selectedServants + newResponsible else selectedServants,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                isServantsDropdownVisible = false
            )
        }
    }

    override fun onRemoveServant(servant: ResponsibleServant) {
        updateState {
            copy(selectedServants = selectedServants.filter { it.id != servant.id })
        }
    }

    override fun onDismissServantSuggestions() {
        updateState { copy(isServantsDropdownVisible = false) }
    }

    override fun onConfirmSaveService() {
        val (isValid, repeatedEvent) = validateAndBuildRepeatedEvent()
        if (!isValid) return

        val input = state.value.serviceNameInput.trim()
        val editing = state.value.editingService
        val stageIds = state.value.selectedStages.map { it.id }
        val servantIds = state.value.selectedServants.map { it.id }

        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = {
                if (editing == null) {
                    attendanceRepository.createService(
                        name = input,
                        educationalStageIds = stageIds,
                        responsibleServantIds = servantIds,
                        repeatedEvent = repeatedEvent
                    )
                } else {
                    attendanceRepository.updateService(
                        id = editing.id,
                        name = input,
                        educationalStageIds = stageIds,
                        responsibleServantIds = servantIds,
                        repeatedEvent = repeatedEvent
                    )
                }
            },
            onSuccess = {
                onDismissSheet()
                loadServices()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_save_service),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    private fun validateAndBuildRepeatedEvent(): Pair<Boolean, ServiceRepeatedEventRequest?> {
        val currentState = state.value
        val serviceName = currentState.serviceNameInput.trim()
        val isNameBlank = serviceName.isBlank()
        val nameError = if (isNameBlank) UiText.StringRes(Res.string.field_required) else null

        var hasError = isNameBlank
        var dateError: UiText? = null
        var repeatError: UiText? = null
        var startError: UiText? = null
        var endError: UiText? = null
        var request: ServiceRepeatedEventRequest? = null

        if (currentState.addRepeatedEvent) {
            val dateStr = currentState.eventDateInput.trim()
            val parsedDate = if (dateStr.isBlank()) {
                dateError = UiText.StringRes(Res.string.field_required)
                hasError = true
                null
            } else {
                parseDate(dateStr) ?: run {
                    dateError = UiText.StringRes(Res.string.field_required)
                    hasError = true
                    null
                }
            }

            val repeatStr = currentState.repeatEvery.trim()
            val repeatValue = if (repeatStr.isBlank()) {
                repeatError = UiText.StringRes(Res.string.field_required)
                hasError = true
                null
            } else {
                val parsed = repeatStr.toIntOrNull()
                if (parsed == null || parsed <= 0) {
                    repeatError = UiText.StringRes(Res.string.repeat_every_must_be_greater_than_zero)
                    hasError = true
                    null
                } else {
                    parsed
                }
            }

            val startStr = currentState.startTimeInput.trim()
            val parsedStart = if (startStr.isBlank()) {
                startError = UiText.StringRes(Res.string.field_required)
                hasError = true
                null
            } else {
                parseTime(startStr) ?: run {
                    startError = UiText.StringRes(Res.string.field_required)
                    hasError = true
                    null
                }
            }

            val endStr = currentState.endTimeInput.trim()
            val parsedEnd = if (endStr.isBlank()) {
                endError = UiText.StringRes(Res.string.field_required)
                hasError = true
                null
            } else {
                parseTime(endStr) ?: run {
                    endError = UiText.StringRes(Res.string.field_required)
                    hasError = true
                    null
                }
            }

            if (parsedStart != null && parsedEnd != null && parsedEnd <= parsedStart) {
                endError = UiText.StringRes(Res.string.end_time_must_be_after_start_time)
                hasError = true
            }

            if (!hasError && parsedDate != null && parsedStart != null && parsedEnd != null && repeatValue != null) {
                val name = currentState.eventNameInput.trim()
                request = ServiceRepeatedEventRequest(
                    name = name.ifBlank { null },
                    startDate = parsedDate,
                    startTime = parsedStart,
                    endTime = parsedEnd,
                    repeatEvery = repeatValue
                )
            }
        }

        if (hasError) {
            updateState {
                copy(
                    serviceNameError = nameError,
                    eventDateError = dateError,
                    repeatEveryError = repeatError,
                    startTimeError = startError,
                    endTimeError = endError
                )
            }
            return Pair(false, null)
        }

        return Pair(true, request)
    }

    override fun onConfirmDeleteService() {
        val service = state.value.deletingService ?: return
        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = { attendanceRepository.deleteService(service.id) },
            onSuccess = {
                onDismissSheet()
                loadServices()
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_delete_service),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onToggleAddRepeatedEvent() {
        updateState {
            copy(
                addRepeatedEvent = !addRepeatedEvent,
                eventNameInput = "",
                eventDateInput = "",
                eventDateError = null,
                repeatEvery = "",
                repeatEveryError = null,
                startTimeInput = "",
                startTimeError = null,
                endTimeInput = "",
                endTimeError = null
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
        updateState {
            copy(
                eventDateInput = date.toString(),
                eventDateError = null,
                isDatePickerOpen = false
            )
        }
    }

    override fun onRepeatEveryChanged(duration: String) {
        val numbersOnly = duration.filter { it.isDigit() }

        if (numbersOnly.isEmpty() || numbersOnly.toIntOrNull()
                ?.let { it > 0 } == true
        ) {
            updateState {
                copy(
                    repeatEvery = numbersOnly,
                    repeatEveryError = null
                )
            }
        }
    }

    override fun onClickStartTimePicker() {
        updateState { copy(isStartTimePickerOpen = true) }
    }

    override fun onDismissStartTimePicker() {
        updateState { copy(isStartTimePickerOpen = false) }
    }

    override fun onStartTimeSelected(time: LocalTime) {
        updateState {
            val end = parseTime(endTimeInput.trim())
            val endErr = if (end != null && end <= time) {
                UiText.StringRes(Res.string.end_time_must_be_after_start_time)
            } else null

            copy(
                startTimeInput = time.formatTime(),
                startTimeError = null,
                endTimeError = endErr,
                isStartTimePickerOpen = false
            )
        }
    }

    override fun onClickEndTimePicker() {
        updateState { copy(isEndTimePickerOpen = true) }
    }

    override fun onDismissEndTimePicker() {
        updateState { copy(isEndTimePickerOpen = false) }
    }

    override fun onEndTimeSelected(time: LocalTime) {
        val start = parseTime(state.value.startTimeInput.trim())
        val endErr = if (start != null && time <= start) {
            UiText.StringRes(Res.string.end_time_must_be_after_start_time)
        } else null

        updateState {
            copy(
                endTimeInput = time.formatTime(),
                endTimeError = endErr,
                isEndTimePickerOpen = false
            )
        }
    }

    override fun onDismissSheet() {
        updateState {
            copy(
                isAddEditSheetOpen = false,
                editingService = null,
                serviceNameInput = "",
                serviceNameError = null,
                selectedStages = emptyList(),
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = emptyList(),
                isDeleteConfirmSheetOpen = false,
                deletingService = null,
                addRepeatedEvent = false,
                eventNameInput = "",
                eventDateInput = "",
                eventDateError = null,
                repeatEvery = "",
                repeatEveryError = null,
                startTimeInput = "",
                startTimeError = null,
                endTimeInput = "",
                endTimeError = null
            )
        }
    }

    override fun onClickService(service: ChurchService) {
        navigate(
            AttendanceEventsRoute(
                serviceId = service.id,
                serviceName = service.name,
                isResponsible = service.isResponsible
            )
        )
    }
}

