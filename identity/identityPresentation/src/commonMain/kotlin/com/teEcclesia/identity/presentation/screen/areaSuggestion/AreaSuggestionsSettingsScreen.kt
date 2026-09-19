package com.teEcclesia.identity.presentation.screen.areaSuggestion

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.style.TextAlign
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.lookups.domain.model.AreaResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_area
import teecclesia.designsystem.generated.resources.area
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.deleta_area_confirmation_message
import teecclesia.designsystem.generated.resources.delete_area_confirmation_title
import teecclesia.designsystem.generated.resources.edit_area
import teecclesia.designsystem.generated.resources.edit_area_suggestions
import teecclesia.designsystem.generated.resources.failed_to_load_area_suggestions
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.no_area_suggestions_found
import teecclesia.designsystem.generated.resources.retry

@Composable
fun AreaSuggestionsSettingScreen(
    viewModel: AreaSuggestionsSettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AreaSuggestionsSettingContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun AreaSuggestionsSettingContent(
    state: AreaSuggestionsSettingsUiState,
    listener: AreaSuggestionsSettingsInteractionListener,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = listener::onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(){
                            IconButton(onClick = listener::onClickBack) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_back),
                                    contentDescription = "Back",
                                    tint = Theme.colorScheme.onBackground
                                )
                            }

                            Text(
                                text = stringResource(Res.string.edit_area_suggestions),
                                style = Theme.typography.headlineSmall,
                                color = Theme.colorScheme.onBackground,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }

                        IconButton(
                            onClick = { listener.onClickAddArea() }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_plus),
                                contentDescription = "Add area",
                                tint = Theme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }


                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Theme.colorScheme.primary)
                        }
                    }
                } else if (state.isLoadFailed) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.failed_to_load_area_suggestions),
                                style = Theme.typography.bodyLarge,
                                color = Theme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppButton(
                                type = AppButtonType.Primary,
                                text = stringResource(Res.string.retry),
                                onClick = listener::onRetryLoad
                            )
                        }
                    }
                }else if (state.areas.isEmpty()){
                    item {
                        Spacer(Modifier.height(300.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_area_suggestions_found),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else {
                    items(
                        state.areas
                    ){ area ->
                        AreaCard(
                            area = area,
                            onEdit = { listener.onClickEditArea(area) },
                            onDelete = { listener.onClickDeleteArea(area) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

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
                        if (state.editingArea == null) Res.string.add_area else Res.string.edit_area
                    ),
                    style = Theme.typography.headlineSmall,
                    color = Theme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = state.areaNameInput,
                    onValueChange = listener::onAreaNameChanged,
                    labelText = stringResource(Res.string.area),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onClickSaveArea,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.confirm),
                    state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled
                )
            }
        }

        BottomSheet(
            isVisible = state.isDeleteConfirmSheetOpen,
            onDismiss = listener::onDismissConfirmDialog
        ) {
            Text(
                text = stringResource(Res.string.delete_area_confirmation_title),
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.deleta_area_confirmation_message),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    type = AppButtonType.Secondary,
                    onClick = listener::onDismissConfirmDialog,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.cancel)
                )

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onConfirmAreaDelete,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.confirm),
                    state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled
                )
            }
        }
    }
}

@Composable
private fun AreaCard(
    area: AreaResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Theme.colorScheme.surfaceContainerHighest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onEdit
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = area.name,
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.onSurface
                )
            }

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
            }
        }
    }
}

@Preview
@Composable
private fun AreaSuggestionsSettingsScreenPreview() = Theme {
    Preview {
        AreaSuggestionsSettingContent(
            state = AreaSuggestionsSettingsUiState(),
            listener = object : AreaSuggestionsSettingsInteractionListener {
                override fun onClickAddArea() {}
                override fun onAreaNameChanged(name: String) {}
                override fun onClickEditArea(areaResponse: AreaResponse) {}
                override fun onClickDeleteArea(areaResponse: AreaResponse) {}
                override fun onClickSaveArea() {}
                override fun onConfirmAreaDelete() {}
                override fun onDismissSheet() {}
                override fun onDismissConfirmDialog() {}
                override fun onClickBack() {}
                override fun onRetryLoad() {}
                override fun onRefresh() {}
            }
        )
    }
}
