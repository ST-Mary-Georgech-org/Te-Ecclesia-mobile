package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.AttendanceEventsRoute
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PageQuery
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_delete_service
import teecclesia.designsystem.generated.resources.failed_to_load_services
import teecclesia.designsystem.generated.resources.failed_to_save_service
import teecclesia.designsystem.generated.resources.maximum_servants_reached
import teecclesia.designsystem.generated.resources.maximum_stages_reached
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
                selectedStages = emptyList(),
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = emptyList()
            )
        }
    }

    override fun onClickEditService(service: ChurchService) {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingService = service,
                serviceNameInput = service.name,
                selectedStages = service.educationalStages,
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = service.responsibleServants
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
        updateState { copy(serviceNameInput = name) }
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
        val input = state.value.serviceNameInput.trim()
        if (input.isBlank()) return

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
                        responsibleServantIds = servantIds
                    )
                } else {
                    attendanceRepository.updateService(
                        id = editing.id,
                        name = input,
                        educationalStageIds = stageIds,
                        responsibleServantIds = servantIds
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

    override fun onDismissSheet() {
        updateState {
            copy(
                isAddEditSheetOpen = false,
                editingService = null,
                serviceNameInput = "",
                selectedStages = emptyList(),
                isStageSheetVisible = false,
                servantSearchQuery = "",
                suggestedServants = emptyList(),
                selectedServants = emptyList(),
                isDeleteConfirmSheetOpen = false,
                deletingService = null
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

