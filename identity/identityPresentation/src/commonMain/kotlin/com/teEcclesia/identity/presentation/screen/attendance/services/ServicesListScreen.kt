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
import com.teEcclesia.identity.domain.model.attendance.ChurchService
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
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.service_name
import teecclesia.designsystem.generated.resources.services

@Composable
fun ServicesListScreen(
    viewModel: ServicesListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ServicesListContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun ServicesListContent(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener,
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.services),
                    style = Theme.typography.headlineMedium,
                    color = Theme.colorScheme.onBackground
                )

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
                    items(state.services, key = { it.id }) { service ->
                        ServiceCard(
                            service = service,
                            onClick = { listener.onClickService(service) },
                            onEdit = { listener.onClickEditService(service) },
                            onDelete = { listener.onClickDeleteService(service) }
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

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onConfirmSaveService,
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
            Text(
                text = service.name,
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                ChurchService(1, "خدمة ابتدائي", getNow()),
                ChurchService(2, "خدمة إعدادي", getNow())
            )
        ),
        listener = object : ServicesListInteractionListener {
            override fun onClickAddService() {}
            override fun onClickEditService(service: ChurchService) {}
            override fun onClickDeleteService(service: ChurchService) {}
            override fun onServiceNameChanged(name: String) {}
            override fun onConfirmSaveService() {}
            override fun onConfirmDeleteService() {}
            override fun onDismissSheet() {}
            override fun onClickService(service: ChurchService) {}
        }
    )
}
