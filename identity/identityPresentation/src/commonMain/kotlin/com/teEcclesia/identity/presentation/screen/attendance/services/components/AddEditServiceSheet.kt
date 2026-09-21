package com.teEcclesia.identity.presentation.screen.attendance.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.sheet.EducationalStageSelectField
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.components.textField.TextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListInteractionListener
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListUiState
import com.teEcclesia.identity.presentation.screen.register.components.UserChip
import com.teEcclesia.lookups.domain.model.LookupResponse
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_repeated_event
import teecclesia.designsystem.generated.resources.add_service
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.edit_service
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.end_time
import teecclesia.designsystem.generated.resources.event_name_optional
import teecclesia.designsystem.generated.resources.event_start_date
import teecclesia.designsystem.generated.resources.ic_calendar
import teecclesia.designsystem.generated.resources.ic_clock
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.repeat_every
import teecclesia.designsystem.generated.resources.responsible_servants
import teecclesia.designsystem.generated.resources.search_servants_hint
import teecclesia.designsystem.generated.resources.select_educational_stage_optional
import teecclesia.designsystem.generated.resources.service_name
import teecclesia.designsystem.generated.resources.start_time

@Composable
fun AddEditServiceSheet(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener
) {
    BottomSheet(
        isVisible = state.isAddEditSheetOpen,
        onDismiss = listener::onDismissSheet
    ) {
        AddEditServiceSheetContent(state, listener)
    }
}

@Composable
private fun AddEditServiceSheetContent(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(
                if (state.editingService == null) Res.string.add_service else Res.string.edit_service
            ),
            style = Theme.typography.headlineSmall,
            color = Theme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = state.serviceNameInput,
            onValueChange = listener::onServiceNameChanged,
            labelText =  stringResource(Res.string.service_name),
            errorText = state.serviceNameError?.asString(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.educational_stage),
                style = Theme.typography.labelMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${state.selectedStages.size}/10",
                style = Theme.typography.labelSmall,
                color = if (state.selectedStages.size >= 10) Theme.colorScheme.error else Theme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        EducationalStageSelectField(
            selectedStages = state.selectedStages,
            educationalStages = state.educationalStages,
            itemTitle = { it.name },
            itemId = { it.id },
            isSelected = { stage -> state.selectedStages.any { it.id == stage.id } },
            isSheetVisible = state.isStageSheetVisible,
            onToggleSheet = listener::onToggleStageSheet,
            onSelectStage = listener::onToggleStageSelection,
            label = stringResource(Res.string.select_educational_stage_optional),
            onLoadNextStages = listener::onLoadNextStages,
            isStageLoading = state.isStageLoading,
            isStageLoadFailed = state.isStageLoadFailed,
            onRetryLoadStages = listener::onRetryLoadStages,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.responsible_servants),
                style = Theme.typography.labelMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${state.selectedServants.size}/30",
                style = Theme.typography.labelSmall,
                color = if (state.selectedServants.size >= 30) Theme.colorScheme.error else Theme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            CustomTextField(
                value = state.servantSearchQuery,
                onValueChange = listener::onServantSearchQueryChanged,
                labelText = stringResource(Res.string.search_servants_hint),
                modifier = Modifier.fillMaxWidth()
            )

            searchServantsDropdown(state, listener)
        }

        if (state.selectedServants.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                state.selectedServants.forEach { servant ->
                    UserChip(
                        user = UserSummary(
                            id = servant.id,
                            name = servant.name,
                            code = servant.code,
                            imageUrl = servant.imageUrl
                        ),
                        onRemove = { listener.onRemoveServant(servant) }
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = state.addRepeatedEvent,
                onCheckedChange = { listener.onToggleAddRepeatedEvent() }
            )

            Text(
                text = stringResource(Res.string.add_repeated_event),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )
        }

        if (state.addRepeatedEvent) {
            Text(
                text = stringResource(Res.string.add_repeated_event),
                style = Theme.typography.titleLarge,
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
                labelText = stringResource(Res.string.event_start_date),
                trailingIcon = painterResource(Res.drawable.ic_calendar),
                onClick = listener::onClickDatePicker,
                errorText = state.eventDateError?.asString(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(
                value = state.repeatEvery,
                onValueChange = { value ->
                    listener.onRepeatEveryChanged(value)
                },
                labelText = stringResource(Res.string.repeat_every),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                errorText = state.repeatEveryError?.asString(),
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
                    errorText = state.startTimeError?.asString(),
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
                    errorText = state.endTimeError?.asString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            type = AppButtonType.Primary,
            onClick = listener::onConfirmSaveService,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.confirm),
            state = when {
                state.isActionLoading -> AppButtonState.Loading
                else -> AppButtonState.Enabled
            }
        )
    }
}

@Composable
private fun searchServantsDropdown(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener
) {
    DropdownMenu(
        expanded = state.isServantsDropdownVisible && (state.suggestedServants.isNotEmpty() || state.isSearchingServants),
        onDismissRequest = listener::onDismissServantSuggestions,
        properties = PopupProperties(focusable = false),
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        if (state.isSearchingServants && state.suggestedServants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Theme.colorScheme.primary
                )
            }
        } else {
            state.suggestedServants.forEach { servant ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (servant.imageUrl.isNullOrBlank()) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                                    contentDescription = null,
                                    tint = Theme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Theme.colorScheme.secondaryContainer)
                                        .padding(6.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = servant.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = servant.name,
                                    style = Theme.typography.titleMedium,
                                    color = Theme.colorScheme.onSurface
                                )
                                val servantCode = servant.code
                                if (!servantCode.isNullOrBlank()) {
                                    Text(
                                        text = servantCode,
                                        style = Theme.typography.labelSmall,
                                        color = Theme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    },
                    onClick = { listener.onSelectServant(servant) }
                )
            }
        }
    }
}


@Composable
@PreviewLightDark
fun AddEditServiceSheetContentPreview() = Theme {
    AddEditServiceSheetContent(
        state = ServicesListUiState(
            isAddEditSheetOpen = true,
            serviceNameInput = "Sunday Service",
            selectedStages = emptyList(),
            educationalStages = emptyList(),
            selectedServants = emptyList(),
            servantSearchQuery = "",
            suggestedServants = emptyList(),
            isServantsDropdownVisible = false,
            addRepeatedEvent = true,
            eventNameInput = "Sunday Service Event",
            eventDateInput = "2024-06-30",
            repeatEvery = "1",
            startTimeInput = "10:00",
            endTimeInput = "12:00"
        ),
        listener = object : ServicesListInteractionListener {
            override fun onDismissSheet() {}
            override fun onClickService(service: ChurchService) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
            override fun onClickAddService() {}
            override fun onClickEditService(service: ChurchService) {}
            override fun onClickDeleteService(service: ChurchService) {}
            override fun onServiceNameChanged(name: String) {}
            override fun onToggleStageSelection(stage: LookupResponse) {}
            override fun onToggleStageSheet(visible: Boolean) {}
            override fun onLoadNextStages() {}
            override fun onRetryLoadStages() {}
            override fun onServantSearchQueryChanged(query: String) {}
            override fun onSelectServant(servant: AttendeeUserPreview) {}
            override fun onRemoveServant(servant: ResponsibleServant) {}
            override fun onDismissServantSuggestions() {}
            override fun onToggleAddRepeatedEvent() {}
            override fun onEventNameChanged(name: String) {}
            override fun onClickDatePicker() {}
            override fun onDismissDatePicker() {}
            override fun onDateSelected(date: LocalDate) {}
            override fun onRepeatEveryChanged(duration: String) {}
            override fun onClickStartTimePicker() {}
            override fun onDismissStartTimePicker() {}
            override fun onStartTimeSelected(time: LocalTime) {}
            override fun onClickEndTimePicker() {}
            override fun onDismissEndTimePicker() {}
            override fun onEndTimeSelected(time: LocalTime) {}
            override fun onConfirmSaveService() {}
            override fun onConfirmDeleteService() {}
        }
    )
}