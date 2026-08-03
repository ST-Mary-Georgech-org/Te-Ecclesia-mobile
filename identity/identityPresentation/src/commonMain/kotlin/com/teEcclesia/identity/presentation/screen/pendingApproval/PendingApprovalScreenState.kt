package com.teEcclesia.identity.presentation.screen.pendingApproval

import com.teEcclesia.designsystem.components.button.AppButtonState

data class PendingApprovalScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val actionButtonState: AppButtonState = AppButtonState.Enabled
)
