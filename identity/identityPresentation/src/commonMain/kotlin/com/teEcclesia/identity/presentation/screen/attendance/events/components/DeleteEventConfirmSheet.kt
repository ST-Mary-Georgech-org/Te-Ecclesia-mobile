package com.teEcclesia.identity.presentation.screen.attendance.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListInteractionListener
import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListUiState
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm_delete_event
import teecclesia.designsystem.generated.resources.delete

@Composable
fun DeleteEventConfirmSheet(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = state.isDeleteConfirmSheetOpen,
        onDismiss = listener::onDismissSheet
    ) {
        DeleteEventConfirmSheetContent(
            state = state,
            listener = listener,
            modifier = modifier
        )
    }
}

@Composable
fun DeleteEventConfirmSheetContent(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
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

@PreviewLightDark
@Composable
private fun DeleteEventConfirmSheetPreview() = Theme {
    DeleteEventConfirmSheetContent(
        state = EventsListUiState(),
        listener = object : EventsListInteractionListener {
            override fun onClickBack() {}
            override fun onClickAddEvent() {}
            override fun onClickEditEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
            override fun onClickDeleteEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
            override fun onEventNameChanged(name: String) {}
            override fun onClickDatePicker() {}
            override fun onDismissDatePicker() {}
            override fun onDateSelected(date: kotlinx.datetime.LocalDate) {}
            override fun onClickStartTimePicker() {}
            override fun onDismissStartTimePicker() {}
            override fun onStartTimeSelected(time: kotlinx.datetime.LocalTime) {}
            override fun onClickEndTimePicker() {}
            override fun onDismissEndTimePicker() {}
            override fun onEndTimeSelected(time: kotlinx.datetime.LocalTime) {}
            override fun onConfirmSaveEvent() {}
            override fun onConfirmDeleteEvent() {}
            override fun onDismissSheet() {}
            override fun onClickEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}
