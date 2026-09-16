package com.teEcclesia.notifications.presentation.screen

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.getLocalizedErrorMessage
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import com.teEcclesia.notifications.domain.util.NotificationReceiveState
import com.teEcclesia.notifications.presentation.util.toPagedData
import com.teEcclesia.shared.domain.utils.PageQuery
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_notifications

class NotificationsViewModel(
    private val notificationRepository: NotificationRepository
) : BaseViewModel<NotificationsUiState>(NotificationsUiState()), NotificationsInteractionListener {

    private val pageSize = 20

    private val notificationsPaginator = createPaginator(
        initialKey = 0,
        loadPage = { page ->
            notificationRepository.getAllNotifications(PageQuery(page = page, size = pageSize)).toPagedData()
        },
        onSuccess = { items ->
            updateState {
                copy(
                    notifications = notifications + items.data,
                    isLoading = false,
                    isRefreshing = false,
                    isError = false,
                    errorMessage = null
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState {
                if (notifications.isEmpty()) {
                    copy(isLoading = loading)
                } else {
                    copy(isLoadingMore = loading)
                }
            }
        },
        onReset = {
            updateState { copy(notifications = emptyList()) }
        },
        onError = { handleError(it) }
    )

    init {
        loadNotifications()
        observeIncomingNotifications()
    }

    private fun observeIncomingNotifications() {
        tryToCollect(
            block = { NotificationReceiveState.receiveFlow },
            onEach = { newNotification ->
                val currentNotifications = state.value.notifications
                if (currentNotifications.none { it.id == newNotification.id }) {
                    updateState {
                        copy(notifications = listOf(newNotification) + notifications)
                    }
                }
            },
            onError = {}
        )
    }

    private fun loadNotifications(isPullToRefresh: Boolean = false) {
        updateState {
            copy(
                isLoading = !isPullToRefresh && notifications.isEmpty(),
                isRefreshing = isPullToRefresh,
                isError = false,
                errorMessage = null
            )
        }
        notificationsPaginator.reset()
        updateState { copy(isRefreshing = false) }

        tryToCall(
            block = { notificationRepository.markAllAsRead() },
            onSuccess = { },
            onError = { }
        )
    }

    private fun handleError(throwable: Throwable?) {
        updateState {
            copy(
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false,
                isError = notifications.isEmpty(),
                errorMessage = UiText.StringRes(Res.string.failed_to_load_notifications)
            )
        }
        if (state.value.notifications.isNotEmpty()) {
            showSnackBar(
                title = UiText.StringRes(Res.string.failed_to_load_notifications),
                message = getLocalizedErrorMessage(throwable),
                isSuccess = false
            )
        }
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onLoadMoreNotifications() {
        notificationsPaginator.loadNextItems()
    }

    override fun onReload() {
        loadNotifications(isPullToRefresh = state.value.notifications.isNotEmpty())
    }
}
