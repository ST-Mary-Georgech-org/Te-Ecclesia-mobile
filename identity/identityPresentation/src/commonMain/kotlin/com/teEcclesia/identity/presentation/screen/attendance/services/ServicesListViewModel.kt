package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.identity.api.AttendanceEventsRoute
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.repository.AttendanceRepository

class ServicesListViewModel(
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<ServicesListUiState>(ServicesListUiState()), ServicesListInteractionListener {

    init {
        loadServices()
    }

    private fun loadServices() {
        tryToCall(
            onStart = { updateState { copy(isLoading = true) } },
            block = { attendanceRepository.getServices() },
            onSuccess = { services ->
                updateState { copy(services = services) }
            },
            onError = { },
            onEnd = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onClickAddService() {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingService = null,
                serviceNameInput = ""
            )
        }
    }

    override fun onClickEditService(service: ChurchService) {
        updateState {
            copy(
                isAddEditSheetOpen = true,
                editingService = service,
                serviceNameInput = service.name
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

    override fun onConfirmSaveService() {
        val input = state.value.serviceNameInput.trim()
        if (input.isBlank()) return

        val editing = state.value.editingService
        tryToCall(
            onStart = { updateState { copy(isActionLoading = true) } },
            block = {
                if (editing == null) {
                    attendanceRepository.createService(input)
                } else {
                    attendanceRepository.updateService(editing.id, input)
                }
            },
            onSuccess = {
                onDismissSheet()
                loadServices()
            },
            onError = { },
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
            onError = { },
            onEnd = { updateState { copy(isActionLoading = false) } }
        )
    }

    override fun onDismissSheet() {
        updateState {
            copy(
                isAddEditSheetOpen = false,
                editingService = null,
                serviceNameInput = "",
                isDeleteConfirmSheetOpen = false,
                deletingService = null
            )
        }
    }

    override fun onClickService(service: ChurchService) {
        navigate(AttendanceEventsRoute(serviceId = service.id, serviceName = service.name))
    }
}
