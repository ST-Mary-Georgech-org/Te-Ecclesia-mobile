package com.teEcclesia.identity.presentation.screen.attendance.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.TextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_event
import teecclesia.designsystem.generated.resources.allowed_period
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.confirm_delete_event
import teecclesia.designsystem.generated.resources.delete
import teecclesia.designsystem.generated.resources.edit_event
import teecclesia.designsystem.generated.resources.end_time
import teecclesia.designsystem.generated.resources.event_date
import teecclesia.designsystem.generated.resources.event_name_optional
import teecclesia.designsystem.generated.resources.events
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.start_time

@Composable
fun EventsListScreen(
    serviceId: Long,
    serviceName: String,
    viewModel: EventsListViewModel = koinViewModel(parameters = { parametersOf(serviceId, serviceName) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EventsListContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun EventsListContent(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
    modifier: Modifier = Modifier
) {
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

                IconButton(onClick = listener::onClickAddEvent) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_plus),
                        contentDescription = "Add event",
                        tint = Theme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.events, key = { it.id }) { event ->
                        EventCard(
                            event = event,
                            onClick = { listener.onClickEvent(event) },
                            onEdit = { listener.onClickEditEvent(event) },
                            onDelete = { listener.onClickDeleteEvent(event) }
                        )
                    }
                }
            }
        }

        BottomSheet(
            isVisible = state.isAddEditSheetOpen,
            onDismiss = listener::onDismissSheet
        ) {
            Text(
                text = stringResource(
                    if (state.editingEvent == null) Res.string.add_event else Res.string.edit_event
                ),
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.eventNameInput,
                onValueChange = listener::onEventNameChanged,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.event_name_optional),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = state.eventDateInput,
                onValueChange = listener::onEventDateChanged,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.event_date),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = state.startTimeInput,
                    onValueChange = listener::onStartTimeChanged,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.start_time),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                TextField(
                    value = state.endTimeInput,
                    onValueChange = listener::onEndTimeChanged,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.end_time),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onConfirmSaveEvent,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.confirm),
                state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled
            )
        }

        BottomSheet(
            isVisible = state.isDeleteConfirmSheetOpen,
            onDismiss = listener::onDismissSheet
        ) {
            Text(
                text = stringResource(Res.string.confirm_delete_event),
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    type = AppButtonType.Secondary,
                    onClick = listener::onDismissSheet,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.cancel)
                )

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onConfirmDeleteEvent,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.delete),
                    state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled,
                    enablePrimaryBackgroundColor = Theme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EventCard(
    event: ServiceEvent,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Theme.colorScheme.surfaceContainerHighest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val eventName = event.name
                if (!eventName.isNullOrBlank()) {
                    Text(
                        text = eventName,
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = event.eventDate.toString(),
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${stringResource(Res.string.allowed_period)}: ${event.startTime} - ${event.endTime}",
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_user_settings),
                        contentDescription = "Edit event",
                        tint = Theme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "Delete event",
                        tint = Theme.colorScheme.error
                    )
                }

                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = "Open event",
                    tint = Theme.colorScheme.onSurfaceVariant
                )
            }
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
            override fun onEventDateChanged(date: String) {}
            override fun onStartTimeChanged(time: String) {}
            override fun onEndTimeChanged(time: String) {}
            override fun onConfirmSaveEvent() {}
            override fun onConfirmDeleteEvent() {}
            override fun onDismissSheet() {}
            override fun onClickEvent(event: ServiceEvent) {}
        }
    )
}
