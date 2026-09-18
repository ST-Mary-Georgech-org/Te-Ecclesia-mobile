package com.teEcclesia.identity.presentation.screen.deletionRequests

import com.teEcclesia.identity.domain.model.AccountDeletionRequest

data class DeletionRequestsUiState(
    val isLoading: Boolean = true,
    val isPagingLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLastPage: Boolean = false,
    val requests: List<AccountDeletionRequest> = emptyList()
)
