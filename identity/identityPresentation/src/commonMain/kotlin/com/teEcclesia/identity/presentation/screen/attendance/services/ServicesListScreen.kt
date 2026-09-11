package com.teEcclesia.identity.presentation.screen.attendance.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.TextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.identity.presentation.shared.components.EducationalStageSelectField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import com.teEcclesia.identity.presentation.screen.register.components.UserChip
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.utils.getNow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_service
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.confirm_delete_service
import teecclesia.designsystem.generated.resources.delete
import teecclesia.designsystem.generated.resources.edit_service
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.no_services_found
import teecclesia.designsystem.generated.resources.no_stage
import teecclesia.designsystem.generated.resources.responsible_servants
import teecclesia.designsystem.generated.resources.search_servants_hint
import teecclesia.designsystem.generated.resources.select_educational_stage_optional
import teecclesia.designsystem.generated.resources.service_name
import teecclesia.designsystem.generated.resources.services

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

        BottomSheet(
            isVisible = state.isAddEditSheetOpen,
            onDismiss = listener::onDismissSheet
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

                TextField(
                    value = state.serviceNameInput,
                    onValueChange = listener::onServiceNameChanged,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.service_name),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                EducationalStageSelectField(
                    selectedStage = state.selectedStage,
                    educationalStages = state.educationalStages,
                    isSheetVisible = state.isStageSheetVisible,
                    onToggleSheet = listener::onToggleStageSheet,
                    onSelectStage = { listener.onStageSelected(it) },
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
                    TextField(
                        value = state.servantSearchQuery,
                        onValueChange = listener::onServantSearchQueryChanged,
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.search_servants_hint),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

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

                Spacer(modifier = Modifier.height(24.dp))

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onConfirmSaveService,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.confirm),
                    state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled
                )
            }
        }

        BottomSheet(
            isVisible = state.isDeleteConfirmSheetOpen,
            onDismiss = listener::onDismissSheet
        ) {
            Text(
                text = stringResource(Res.string.confirm_delete_service),
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
                    onClick = listener::onConfirmDeleteService,
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
private fun ServiceCard(
    service: ChurchService,
    isAdmin: Boolean,
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
                Text(
                    text = service.name,
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.onSurface
                )
                val stageName = service.educationalStageName
                if (!stageName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Theme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = stageName,
                            style = Theme.typography.labelSmall,
                            color = Theme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isAdmin) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_user_settings),
                            contentDescription = "Edit service",
                            tint = Theme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Delete service",
                            tint = Theme.colorScheme.error
                        )
                    }
                }

                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = "Open service",
                    tint = Theme.colorScheme.onSurfaceVariant
                )
            }
        }
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
                    educationalStageId = null,
                    educationalStageName = null,
                    responsibleServants = emptyList()
                ),
                ChurchService(
                    id = 2,
                    name = "خدمة إعدادي",
                    createdAt = getNow(),
                    isResponsible = false,
                    educationalStageId = 1L,
                    educationalStageName = "إعدادي",
                    responsibleServants = emptyList()
                )
            )
        ),
        listener = object : ServicesListInteractionListener {
            override fun onClickAddService() {}
            override fun onClickEditService(service: ChurchService) {}
            override fun onClickDeleteService(service: ChurchService) {}
            override fun onServiceNameChanged(name: String) {}
            override fun onStageSelected(stage: LookupResponse?) {}
            override fun onToggleStageSheet(visible: Boolean) {}
            override fun onLoadNextStages() {}
            override fun onRetryLoadStages() {}
            override fun onServantSearchQueryChanged(query: String) {}
            override fun onSelectServant(servant: AttendeeUserPreview) {}
            override fun onRemoveServant(servant: ResponsibleServant) {}
            override fun onDismissServantSuggestions() {}
            override fun onConfirmSaveService() {}
            override fun onConfirmDeleteService() {}
            override fun onDismissSheet() {}
            override fun onClickService(service: ChurchService) {}
            override fun onRefresh() {}
            override fun onLoadMore() {}
        }
    )
}

