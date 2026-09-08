package com.teEcclesia.notifications.presentation.screen

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import com.teEcclesia.notifications.domain.util.NotificationReceiveState
import com.teEcclesia.notifications.presentation.util.toPagedData
import com.teEcclesia.shared.domain.utils.PageQuery
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.error_occurred
import teecclesia.designsystem.generated.resources.unknown_error

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
            updateState { copy(notifications = notifications + items.data) }
        },
        onLoadUpdated = { isLoadingMore ->
            updateState { copy(isLoadingMore = isLoadingMore) }
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

    private fun loadNotifications() {
        updateState { copy(isLoading = true, isRefreshing = true) }
        notificationsPaginator.reset()
        updateState { copy(isLoading = false, isRefreshing = false) }

        tryToCall(
            block = { notificationRepository.markAllAsRead() },
            onSuccess = { },
            onError = { }
        )
    }

    private fun handleError(throwable: Throwable?) {
        showSnackBar(
            title = UiText.StringRes(Res.string.error_occurred),
            message = throwable?.message?.let(UiText::DynamicString)
                ?: UiText.StringRes(Res.string.unknown_error),
            isSuccess = false
        )
    }

    override fun onClickBack() {
        popBackStack()
    }

    override fun onLoadMoreNotifications() {
        updateState { copy(isLoadingMore = true) }
        notificationsPaginator.loadNextItems()
        updateState { copy(isLoadingMore = false) }
    }

    override fun onReload() {
        loadNotifications()
    }
}
