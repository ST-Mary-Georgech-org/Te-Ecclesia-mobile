package com.teEcclesia.identity.presentation.screen.requests

import androidx.lifecycle.viewModelScope
import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.api.ReviewAndEditRequestRoute
import com.teEcclesia.identity.domain.model.UserRole

import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.failed_to_load_requests
import kotlin.time.Duration.Companion.milliseconds

class RegistrationRequestsViewModel(
    private val profileRepository: ProfileRepository
) : BaseViewModel<RegistrationRequestsUiState>(RegistrationRequestsUiState()),
    RegistrationRequestsInteractionListener {

    private var searchJob: Job? = null
    private val pageSize = 20

    private val requestsPaginator = createPaginator(
        loadPage = { page ->
            profileRepository.getRegistrationRequests(
                role = state.value.selectedRole,
                search = state.value.searchQuery.takeIf { it.isNotBlank() },
                page = page,
                size = pageSize,
                sortBy = state.value.sortBy,
                sortOrder = state.value.sortOrder
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    requests = requests + items.data,
                    isLastPage = items.isLastPage,
                    page = if (!items.isLastPage) current.page + 1 else current.page
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.requests.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onError = { throwable ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    isError = true
                )
            }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_requests),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        loadRequests()
    }

    private fun loadRequests() {
        launch {
            updateState { it.copy(page = 0, requests = emptyList()) }
            requestsPaginator.reset()
            requestsPaginator.loadNextItems()
        }
    }

    override fun onSearchQueryChanged(query: String) {
        updateState { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = launch {
            delay(500.milliseconds)
            loadRequests()
        }
    }

    override fun onRoleFilterSelected(role: UserRole?) {
        updateState { it.copy(selectedRole = role) }
        loadRequests()
    }

    override fun onSortByChanged(sortBy: String, sortOrder: String) {
        updateState { it.copy(sortBy = sortBy, sortOrder = sortOrder) }
        loadRequests()
    }

    override fun onToggleSortingSheet(isVisible: Boolean) {
        updateState { it.copy(isSortingSheetVisible = isVisible) }
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            launch {
                requestsPaginator.loadNextItems()
            }
        }
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadRequests()
    }

    override fun onApproveUser(userId: String) {
        tryToCall(
            block = { profileRepository.approveUser(userId) },
            onSuccess = {
                val updatedRequests = state.value.requests.filter { it.id != userId }
                updateState { it.copy(requests = updatedRequests) }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.error_occurred),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }

    override fun onRejectUser(userId: String, reason: String) {
        tryToCall(
            block = { profileRepository.rejectUser(userId, reason) },
            onSuccess = {
                val updatedRequests = state.value.requests.filter { it.id != userId }
                updateState { it.copy(requests = updatedRequests) }
            },
            onError = { throwable ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.error_occurred),
                    message = getLocalizedErrorMessage(throwable),
                    isSuccess = false
                )
            }
        )
    }

    override fun onRequestClicked(userId: String) {
        navigate(ReviewAndEditRequestRoute(userId))
    }

    override fun onClickBack() {
        popBackStack()
    }
}

