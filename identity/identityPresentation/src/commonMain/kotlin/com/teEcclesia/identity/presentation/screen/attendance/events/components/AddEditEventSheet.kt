package com.teEcclesia.identity.presentation.screen.attendance.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.dialog.DatePicker
import com.teEcclesia.designsystem.components.dialog.TimePickerDialog
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListInteractionListener
import com.teEcclesia.identity.presentation.screen.attendance.events.EventsListUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_event
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.edit_event
import teecclesia.designsystem.generated.resources.end_time
import teecclesia.designsystem.generated.resources.event_date
import teecclesia.designsystem.generated.resources.event_name_optional
import teecclesia.designsystem.generated.resources.ic_calendar
import teecclesia.designsystem.generated.resources.ic_clock
import teecclesia.designsystem.generated.resources.start_time

@Composable
fun AddEditEventSheet(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = state.isAddEditSheetOpen,
        onDismiss = listener::onDismissSheet
    ) {
        AddEditEventSheetContent(
            state = state,
            listener = listener,
            modifier = modifier
        )
    }

    DatePicker(
        showDialog = state.isDatePickerOpen,
        selectedDate = runCatching { LocalDate.parse(state.eventDateInput) }.getOrNull(),
        onDateSelected = listener::onDateSelected,
        onDismiss = listener::onDismissDatePicker
    )

    TimePickerDialog(
        showDialog = state.isStartTimePickerOpen,
        initialTime = runCatching { LocalTime.parse(state.startTimeInput) }.getOrNull(),
        title = stringResource(Res.string.start_time),
        onTimeSelected = listener::onStartTimeSelected,
        onDismiss = listener::onDismissStartTimePicker
    )

    TimePickerDialog(
        showDialog = state.isEndTimePickerOpen,
        initialTime = runCatching { LocalTime.parse(state.endTimeInput) }.getOrNull(),
        title = stringResource(Res.string.end_time),
        onTimeSelected = listener::onEndTimeSelected,
        onDismiss = listener::onDismissEndTimePicker
    )
}

@Composable
fun AddEditEventSheetContent(
    state: EventsListUiState,
    listener: EventsListInteractionListener,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        Text(
            text = stringResource(
                if (state.editingEvent == null) Res.string.add_event else Res.string.edit_event
            ),
            style = Theme.typography.headlineSmall,
            color = Theme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = state.eventNameInput,
            onValueChange = listener::onEventNameChanged,
            labelText = stringResource(Res.string.event_name_optional),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomTextField(
            value = state.eventDateInput,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            labelText = stringResource(Res.string.event_date),
            trailingIcon = painterResource(Res.drawable.ic_calendar),
            onClick = listener::onClickDatePicker,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomTextField(
                value = state.startTimeInput,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                labelText = stringResource(Res.string.start_time),
                trailingIcon = painterResource(Res.drawable.ic_clock),
                onClick = listener::onClickStartTimePicker,
                modifier = Modifier.weight(1f)
            )

            CustomTextField(
                value = state.endTimeInput,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                labelText = stringResource(Res.string.end_time),
                trailingIcon = painterResource(Res.drawable.ic_clock),
                onClick = listener::onClickEndTimePicker,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val isFormValid = state.eventDateInput.isNotBlank() &&
                state.startTimeInput.isNotBlank() &&
                state.endTimeInput.isNotBlank()

        AppButton(
            type = AppButtonType.Primary,
            onClick = listener::onConfirmSaveEvent,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.confirm),
            state = when {
                state.isActionLoading -> AppButtonState.Loading
                !isFormValid -> AppButtonState.Disabled
                else -> AppButtonState.Enabled
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@PreviewLightDark
@Composable
private fun AddEditEventSheetPreview() = Theme {
    AddEditEventSheetContent(
        state = EventsListUiState(
            eventDateInput = "2026-07-30",
            startTimeInput = "18:00",
            endTimeInput = "20:00"
        ),
        listener = object : EventsListInteractionListener {
            override fun onClickBack() {}
            override fun onClickAddEvent() {}
            override fun onClickEditEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
            override fun onClickDeleteEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
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
            override fun onClickEvent(event: com.teEcclesia.identity.domain.model.attendance.ServiceEvent) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}
