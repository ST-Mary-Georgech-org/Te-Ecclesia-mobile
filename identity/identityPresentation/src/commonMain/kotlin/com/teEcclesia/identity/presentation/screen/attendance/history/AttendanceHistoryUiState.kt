package com.teEcclesia.identity.presentation.screen.attendance.history

import com.teEcclesia.identity.domain.model.attendance.UserAttendanceHistory

data class AttendanceHistoryUiState(
    val userId: String = "",
    val userName: String = "",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isPagingLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val history: List<UserAttendanceHistory> = emptyList(),
    val totalItems: Long = 0L
)
