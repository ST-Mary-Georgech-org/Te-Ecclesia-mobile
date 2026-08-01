package com.teEcclesia.identity.presentation.screen.attendance.services

import com.teEcclesia.identity.domain.model.attendance.ChurchService

data class ServicesListUiState(
    val isLoading: Boolean = false,
    val services: List<ChurchService> = emptyList(),
    val isAddEditSheetOpen: Boolean = false,
    val editingService: ChurchService? = null,
    val serviceNameInput: String = "",
    val isDeleteConfirmSheetOpen: Boolean = false,
    val deletingService: ChurchService? = null,
    val isActionLoading: Boolean = false
)
