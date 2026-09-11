package com.teEcclesia.identity.presentation.screen.academicYear

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText

data class AcademicYearSettingsUiState(
    val academicYear: String = "",
    val academicYearError: UiText? = null,
    val isLoading: Boolean = true,
    val isLoadFailed: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSaving: Boolean = false,
    val isConfirmDialogOpen: Boolean = false,
    val saveButtonState: AppButtonState = AppButtonState.Enabled
)
