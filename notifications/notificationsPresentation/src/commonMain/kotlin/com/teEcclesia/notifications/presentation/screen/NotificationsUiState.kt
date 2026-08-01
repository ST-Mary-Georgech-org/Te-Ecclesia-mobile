package com.teEcclesia.notifications.presentation.screen

import com.teEcclesia.notifications.domain.model.NotificationResponse

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val notifications: List<NotificationResponse> = emptyList()
)
