package com.teEcclesia.identity.presentation.screen.attendance.history

import com.teEcclesia.designsystem.navigation.BaseViewModel
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.repository.AttendanceRepository
import com.teEcclesia.identity.presentation.util.getLocalizedErrorMessage
import com.teEcclesia.identity.presentation.util.toPagedData
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_attendance_history

class AttendanceHistoryViewModel(
    userId: String,
    userName: String = "",
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel<AttendanceHistoryUiState>(
    AttendanceHistoryUiState(userId = userId, userName = userName)
), AttendanceHistoryInteractionListener {

    private val pageSize = 20

    private val historyPaginator = createPaginator(
        loadPage = { page ->
            attendanceRepository.getUserAttendanceHistory(
                userId = state.value.userId,
                page = page,
                size = pageSize
            ).toPagedData()
        },
        onSuccess = { items ->
            updateState { current ->
                current.copy(
                    isLoading = false,
                    isPagingLoading = false,
                    isRefreshing = false,
                    history = current.history + items.data,
                    totalItems = items.totalItems,
                    isLastPage = items.isLastPage
                )
            }
        },
        onLoadUpdated = { loading ->
            updateState { current ->
                if (current.history.isEmpty() && !current.isRefreshing) {
                    current.copy(isLoading = loading)
                } else if (!current.isRefreshing) {
                    current.copy(isPagingLoading = loading)
                } else {
                    current
                }
            }
        },
        onReset = {
            updateState { it.copy(history = emptyList(), isLastPage = false) }
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
                    title = UiText.StringRes(Res.string.failed_to_load_attendance_history),
                    message = getLocalizedErrorMessage(t),
                    isSuccess = false
                )
            }
        }
    )

    init {
        loadHistory()
    }

    private fun loadHistory() {
        historyPaginator.reset()
    }

    override fun onRefresh() {
        updateState { it.copy(isRefreshing = true) }
        loadHistory()
    }

    override fun onLoadMore() {
        if (!state.value.isLastPage && !state.value.isPagingLoading && !state.value.isLoading) {
            historyPaginator.loadNextItems()
        }
    }

    override fun onClickBack() {
        popBackStack()
    }
}
