package com.teEcclesia.identity.presentation.screen.areaSuggestion

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.lookups.domain.model.AreaResponse
import com.teEcclesia.lookups.domain.model.LookupResponse

data class AreaSuggestionsSettingsUiState(
    val areas: List<AreaResponse> = emptyList(),
    val areasError: UiText? = null,
    val isAddEditSheetOpen: Boolean = false,
    val editingArea: AreaResponse? = null,
    val areaNameInput: String = "",
    val isLoading: Boolean = true,
    val isLoadFailed: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEditAreaSheetVisible : Boolean = false,
    val isDeleteConfirmSheetOpen: Boolean = false,
    val deletingArea: AreaResponse? = null,
    val isSaving: Boolean = false,
    val isActionLoading : Boolean = false,
    val saveButtonState: AppButtonState = AppButtonState.Enabled
)
