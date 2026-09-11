package com.teEcclesia.identity.presentation.screen.attendance.events

import com.teEcclesia.identity.domain.model.attendance.ServiceEvent

data class EventsListUiState(
    val serviceId: Long = 0,
    val serviceName: String = "",
    val isResponsible: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPagingLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val totalEvents: Long = 0L,
    val events: List<ServiceEvent> = emptyList(),
    val isAddEditSheetOpen: Boolean = false,
    val editingEvent: ServiceEvent? = null,
    val eventNameInput: String = "",
    val eventDateInput: String = "",
    val startTimeInput: String = "",
    val endTimeInput: String = "",
    val isDatePickerOpen: Boolean = false,
    val isStartTimePickerOpen: Boolean = false,
    val isEndTimePickerOpen: Boolean = false,
    val isDeleteConfirmSheetOpen: Boolean = false,
    val deletingEvent: ServiceEvent? = null,
    val isActionLoading: Boolean = false
)
