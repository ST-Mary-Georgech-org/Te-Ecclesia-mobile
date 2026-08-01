package com.teEcclesia.identity.presentation.screen.attendance.register

import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.EventAttendee

data class AttendanceRegisterUiState(
    val eventId: Long = 0,
    val serviceName: String = "",
    val eventName: String = "",
    val isLoading: Boolean = false,
    val attendees: List<EventAttendee> = emptyList(),
    val isAddPersonSheetOpen: Boolean = false,
    val userCodeInput: String = "",
    val searchedUser: AttendeeUserPreview? = null,
    val isSearchingUser: Boolean = false,
    val searchUserError: String? = null,
    val isRemoveConfirmSheetOpen: Boolean = false,
    val removingAttendee: EventAttendee? = null,
    val isActionLoading: Boolean = false
)
