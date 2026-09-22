package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.lookups.domain.model.LookupResponse

data class ServicesListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPagingLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val totalServices: Long = 0L,
    val services: List<ChurchService> = emptyList(),
    val isAdmin: Boolean = false,
    val isAddEditSheetOpen: Boolean = false,
    val editingService: ChurchService? = null,
    val serviceNameInput: String = "",
    val serviceNameError: UiText? = null,
    val educationalStages: List<LookupResponse> = emptyList(),
    val selectedStages: List<LookupResponse> = emptyList(),
    val isStageSheetVisible: Boolean = false,
    val isStageLoading: Boolean = false,
    val isStageLoadFailed: Boolean = false,
    val isStageLastPage: Boolean = false,
    val isStagePagingLoading: Boolean = false,
    val servantSearchQuery: String = "",
    val suggestedServants: List<AttendeeUserPreview> = emptyList(),
    val isSearchingServants: Boolean = false,
    val isServantsDropdownVisible: Boolean = false,
    val selectedServants: List<ResponsibleServant> = emptyList(),
    val addRepeatedEvent : Boolean = false,
    val isDatePickerOpen : Boolean = false,
    val eventNameInput : String = "",
    val eventDateInput : String = "",
    val eventDateError: UiText? = null,
    val repeatEvery : String = "",
    val repeatEveryError: UiText? = null,
    val startTimeInput: String = "",
    val startTimeError: UiText? = null,
    val endTimeInput: String = "",
    val endTimeError: UiText? = null,
    val isStartTimePickerOpen: Boolean = false,
    val isEndTimePickerOpen: Boolean = false,
    val isDeleteConfirmSheetOpen: Boolean = false,
    val deletingService: ChurchService? = null,
    val isActionLoading: Boolean = false
)
