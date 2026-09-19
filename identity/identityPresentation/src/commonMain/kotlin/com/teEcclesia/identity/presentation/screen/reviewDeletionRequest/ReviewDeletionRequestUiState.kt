package com.teEcclesia.identity.presentation.screen.reviewDeletionRequest

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.shared.domain.model.UserRole

data class ReviewDeletionRequestUiState(
    val requestId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userCode: String? = null,
    val userImageUrl: String? = null,
    val userRole: UserRole? = null,
    val reason: String = "",
    val requestedAt: String = "",
    val approveButtonState: AppButtonState = AppButtonState.Enabled,
    val rejectButtonState: AppButtonState = AppButtonState.Enabled
)
