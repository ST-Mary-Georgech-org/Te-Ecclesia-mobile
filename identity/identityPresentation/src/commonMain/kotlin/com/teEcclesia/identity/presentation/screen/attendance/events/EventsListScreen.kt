package com.teEcclesia.identity.presentation.screen.attendance.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.identity.presentation.screen.attendance.events.components.AddEditEventSheet
import com.teEcclesia.identity.presentation.screen.attendance.events.components.DeleteEventConfirmSheet
import com.teEcclesia.identity.presentation.screen.attendance.events.components.EventCard
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.events
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.no_events_found

@Composable
fun EventsListScreen(
    serviceId: Long,
    serviceName: String,
    isResponsible: Boolean,
    viewModel: EventsListViewModel = koinViewModel(parameters = { parametersOf(serviceId, serviceName, isResponsible) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        EventsListContent(
            state = state,
            listener = viewModel
        )
    }
}

@Composable
private fun EventsListContent(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

                    Text(
                        text = state.serviceName.ifBlank { stringResource(Res.string.events) },
                        style = Theme.typography.headlineSmall,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (state.isResponsible) {
                    IconButton(onClick = listener::onClickAddEvent) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = "Add event",
                            tint = Theme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading && !state.isRefreshing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else if (state.events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_events_found),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.events, key = { it.id }) { event ->
                        EventCard(
                            event = event,
                            isResponsible = state.isResponsible,
                            onClick = { listener.onClickEvent(event) },
                            onEdit = { listener.onClickEditEvent(event) },
                            onDelete = { listener.onClickDeleteEvent(event) }
                        )
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
            list = state.events,
            listState = listState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadMore
        )

        if (state.isResponsible) {
            AddEditEventSheet(
                state = state,
                listener = listener
            )

            DeleteEventConfirmSheet(
                state = state,
                listener = listener
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun EventsListPreview() = Theme {
    EventsListContent(
        state = EventsListUiState(
            serviceName = "خدمة ابتدائي",
            events = listOf(
                ServiceEvent(1, 1, "اجتماع الجمعه", LocalDate.parse("2026-07-30"), LocalTime(18, 0), LocalTime(20, 0), getNow())
            )
        ),
        listener = object : EventsListInteractionListener {
            override fun onClickBack() {}
            override fun onClickAddEvent() {}
            override fun onClickEditEvent(event: ServiceEvent) {}
            override fun onClickDeleteEvent(event: ServiceEvent) {}
            override fun onEventNameChanged(name: String) {}
            override fun onClickDatePicker() {}
            override fun onDismissDatePicker() {}
            override fun onDateSelected(date: LocalDate) {}
            override fun onClickStartTimePicker() {}
            override fun onDismissStartTimePicker() {}
            override fun onStartTimeSelected(time: LocalTime) {}
            override fun onClickEndTimePicker() {}
            override fun onDismissEndTimePicker() {}
            override fun onEndTimeSelected(time: LocalTime) {}
            override fun onConfirmSaveEvent() {}
            override fun onConfirmDeleteEvent() {}
            override fun onDismissSheet() {}
            override fun onClickEvent(event: ServiceEvent) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}
