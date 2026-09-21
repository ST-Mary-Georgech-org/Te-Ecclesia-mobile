package com.teEcclesia.identity.presentation.screen.attendance.services

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.teEcclesia.designsystem.components.dialog.DatePicker
import com.teEcclesia.designsystem.components.dialog.TimePickerDialog
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.identity.presentation.screen.attendance.services.components.AddEditServiceSheet
import com.teEcclesia.identity.presentation.screen.attendance.services.components.DeleteServiceSheet
import com.teEcclesia.identity.presentation.screen.attendance.services.components.ServiceCard
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.utils.getNow
import com.teEcclesia.shared.domain.utils.parseDate
import com.teEcclesia.shared.domain.utils.parseTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm_delete_service
import teecclesia.designsystem.generated.resources.delete
import teecclesia.designsystem.generated.resources.end_time
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.no_services_found
import teecclesia.designsystem.generated.resources.services
import teecclesia.designsystem.generated.resources.start_time

@Composable
fun ServicesListScreen(
    viewModel: ServicesListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        ServicesListContent(
            state = state,
            listener = viewModel
        )
    }

    DatePicker(
        showDialog = state.isDatePickerOpen,
        selectedDate = parseDate(state.eventDateInput),
        onDateSelected = viewModel::onDateSelected,
        onDismiss = viewModel::onDismissDatePicker
    )

    TimePickerDialog(
        showDialog = state.isStartTimePickerOpen,
        initialTime = parseTime(state.startTimeInput),
        title = stringResource(Res.string.start_time),
        onTimeSelected = viewModel::onStartTimeSelected,
        onDismiss = viewModel::onDismissStartTimePicker
    )

    TimePickerDialog(
        showDialog = state.isEndTimePickerOpen,
        initialTime = parseTime(state.endTimeInput),
        title = stringResource(Res.string.end_time),
        onTimeSelected = viewModel::onEndTimeSelected,
        onDismiss = viewModel::onDismissEndTimePicker
    )
}

@Composable
private fun ServicesListContent(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener,
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.services),
                    style = Theme.typography.headlineMedium,
                    color = Theme.colorScheme.onBackground
                )

                if (state.isAdmin) {
                    IconButton(
                        onClick = listener::onClickAddService
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = "Add service",
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
            } else if (state.services.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_services_found),
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
                    items(state.services, key = { it.id }) { service ->
                        ServiceCard(
                            service = service,
                            isAdmin = state.isAdmin,
                            onClick = { listener.onClickService(service) },
                            onEdit = { listener.onClickEditService(service) },
                            onDelete = { listener.onClickDeleteService(service) }
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
            list = state.services,
            listState = listState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadMore
        )

        AddEditServiceSheet(state, listener)

        DeleteServiceSheet(state, listener)
    }
}


@PreviewLightDark
@Composable
private fun ServicesListPreview() = Theme {
    ServicesListContent(
        state = ServicesListUiState(
            services = listOf(
                ChurchService(
                    id = 1,
                    name = "خدمة ابتدائي",
                    createdAt = getNow(),
                    isResponsible = true,
                    educationalStages = emptyList(),
                    responsibleServants = emptyList(),
                    repeatedEvent = null
                ),
                ChurchService(
                    id = 2,
                    name = "خدمة إعدادي",
                    createdAt = getNow(),
                    isResponsible = false,
                    educationalStages = listOf(
                        LookupResponse(
                            id = 1L,
                            name = "إعدادي",
                            subItems = emptyList()
                        )
                    ),
                    responsibleServants = emptyList(),
                    repeatedEvent = null
                )
            )
        ),
        listener = object : ServicesListInteractionListener {
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
            override fun onConfirmSaveService() {}
            override fun onConfirmDeleteService() {}
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
            override fun onDismissSheet() {}
            override fun onClickService(service: ChurchService) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}

