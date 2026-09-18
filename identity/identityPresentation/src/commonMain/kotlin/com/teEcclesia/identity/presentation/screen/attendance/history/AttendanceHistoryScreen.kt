package com.teEcclesia.identity.presentation.screen.attendance.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.attendance.UserAttendanceHistory
import com.teEcclesia.identity.presentation.screen.attendance.history.components.AttendanceHistoryCard
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.attendance_history
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.no_attendance_history_found

@Composable
fun AttendanceHistoryScreen(
    userId: String,
    userName: String = "",
    viewModel: AttendanceHistoryViewModel = koinViewModel(parameters = { parametersOf(userId, userName) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        AttendanceHistoryContent(
            state = state,
            listener = viewModel
        )
    }
}

@Composable
private fun AttendanceHistoryContent(
    state: AttendanceHistoryUiState,
    listener: AttendanceHistoryInteractionListener,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = listener::onClickBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Theme.colorScheme.onBackground
                        )
                    }

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = stringResource(Res.string.attendance_history),
                            style = Theme.typography.headlineSmall,
                            color = Theme.colorScheme.onBackground
                        )
                        if (state.userName.isNotBlank()) {
                            Text(
                                text = state.userName,
                                style = Theme.typography.bodySmall,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Theme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${state.totalItems}",
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (state.isLoading && !state.isRefreshing) {
                    item(key = "initial_loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Theme.colorScheme.primary)
                        }
                    }
                } else if (state.history.isEmpty()) {
                    item(key = "empty_history") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(0.7f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_attendance_history_found),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(state.history, key = { it.id }) { item ->
                        AttendanceHistoryCard(history = item)
                    }

                    if (state.isPagingLoading) {
                        item(key = "paging_loading") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Theme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        PaginationTrigger(
            list = state.history,
            listState = listState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadMore
        )
    }
}

@PreviewLightDark
@Composable
private fun AttendanceHistoryPreview() = Theme {
    AttendanceHistoryContent(
        state = AttendanceHistoryUiState(
            userName = "ماريو عماد",
            totalItems = 1,
            isLoading = false,
            history = listOf(
                UserAttendanceHistory(
                    id = 1,
                    eventId = 1,
                    serviceId = 1,
                    serviceName = "اجتماع إعدادي",
                    eventName = "درس الجمعة",
                    eventDate = LocalDate(2026, 9, 12),
                    startTime = LocalTime(18, 0),
                    endTime = LocalTime(20, 0),
                    registeredAt = getNow()
                )
            )
        ),
        listener = object : AttendanceHistoryInteractionListener {
            override fun onClickBack() {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}
