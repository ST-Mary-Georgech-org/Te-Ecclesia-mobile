package com.teEcclesia.identity.presentation.screen.deletionRequests

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.identity.api.ReviewDeletionRequestRoute
import com.teEcclesia.identity.domain.model.AccountDeletionRequest
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.presentation.util.toPagedData
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_deletion_requests

class DeletionRequestsViewModel(
    private val profileRepository: ProfileRepository
) : BaseViewModel<DeletionRequestsUiState>(DeletionRequestsUiState()), DeletionRequestsInteractionListener {

    private val pageSize = 20

    private val requestsPaginator = createPaginator(
        initialKey = 0,
        loadPage = { page ->
            profileRepository.getDeletionRequests(page = page, size = pageSize).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    requests = current.requests + items.data,
                    isLastPage = items.isLastPage
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
        onReset = {
            updateState { it.copy(requests = emptyList(), isLastPage = false) }
        },
        onError = { throwable ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false
                )
            }
            throwable?.let { t ->
                showSnackBar(
                    title = UiText.StringRes(Res.string.failed_to_load_deletion_requests),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        loadRequests()
        listenForHandledRequests()
    }

    private fun loadRequests() {
        requestsPaginator.reset()
    }

    private fun listenForHandledRequests() {
        launch {
            getResult<Boolean>(KEY_DELETION_REQUEST_HANDLED, consume = true).collect { isHandled ->
                if (isHandled == true) {
                    loadRequests()
                }
            }
        }
    }

    override fun onRefresh() {
        updateState { copy(isRefreshing = true) }
        requestsPaginator.reset()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            requestsPaginator.loadNextItems()
        }
    }

    override fun onRequestClicked(request: AccountDeletionRequest) {
        navigate(
            ReviewDeletionRequestRoute(
                requestId = request.id,
                userId = request.userId,
                userName = request.userName,
                userCode = request.userCode,
                userImageUrl = request.userImageUrl,
                userRole = request.userRole.name,
                reason = request.reason,
                requestedAt = request.requestedAt.toString()
            )
        )
    }

    override fun onClickBack() {
        popBackStack()
    }

    companion object {
        const val KEY_DELETION_REQUEST_HANDLED = "KEY_DELETION_REQUEST_HANDLED"
    }
}
