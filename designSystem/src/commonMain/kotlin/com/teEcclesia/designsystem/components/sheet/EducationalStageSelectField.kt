package com.teEcclesia.designsystem.components.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.no_stage
import teecclesia.designsystem.generated.resources.ok

/**
 * Single-select Generic Educational Stage field overload (with optional clear support).
 */
@Composable
fun <T> EducationalStageSelectField(
    selectedStage: T?,
    educationalStages: List<T>,
    itemTitle: (T) -> String,
    itemId: (T) -> Any,
    isSelected: (T) -> Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (T?) -> Unit,
    label: String = stringResource(Res.string.educational_stage),
    allowClear: Boolean = false,
    onLoadNextStages: () -> Unit = {},
    isStageLoading: Boolean = false,
    isStageLoadFailed: Boolean = false,
    onRetryLoadStages: () -> Unit = {},
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalStageSelectContent(
        displayValue = selectedStage?.let(itemTitle) ?: "",
        label = label,
        educationalStages = educationalStages,
        itemTitle = itemTitle,
        itemId = itemId,
        isMultiSelect = false,
        isSelected = isSelected,
        isNoneSelected = selectedStage == null,
        allowClear = allowClear,
        isSheetVisible = isSheetVisible,
        onToggleSheet = onToggleSheet,
        onSelectStage = onSelectStage,
        onLoadNextStages = onLoadNextStages,
        isStageLoading = isStageLoading,
        isStageLoadFailed = isStageLoadFailed,
        onRetryLoadStages = onRetryLoadStages,
        errorText = errorText,
        modifier = modifier
    )
}

/**
 * Single-select Generic Educational Stage field overload (mandatory selection).
 */
@Composable
fun <T> EducationalStageSelectField(
    selectedStage: T?,
    educationalStages: List<T>,
    itemTitle: (T) -> String,
    itemId: (T) -> Any,
    isSelected: (T) -> Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (T) -> Unit,
    label: String = stringResource(Res.string.educational_stage),
    onLoadNextStages: () -> Unit = {},
    isStageLoading: Boolean = false,
    isStageLoadFailed: Boolean = false,
    onRetryLoadStages: () -> Unit = {},
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalStageSelectField(
        selectedStage = selectedStage,
        educationalStages = educationalStages,
        itemTitle = itemTitle,
        itemId = itemId,
        isSelected = isSelected,
        isSheetVisible = isSheetVisible,
        onToggleSheet = onToggleSheet,
        onSelectStage = { stage -> if (stage != null) onSelectStage(stage) },
        label = label,
        allowClear = false,
        onLoadNextStages = onLoadNextStages,
        isStageLoading = isStageLoading,
        isStageLoadFailed = isStageLoadFailed,
        onRetryLoadStages = onRetryLoadStages,
        errorText = errorText,
        modifier = modifier
    )
}

/**
 * Multi-select Generic Educational Stage field overload.
 */
@Composable
fun <T> EducationalStageSelectField(
    selectedStages: List<T>,
    educationalStages: List<T>,
    itemTitle: (T) -> String,
    itemId: (T) -> Any,
    isSelected: (T) -> Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (T) -> Unit,
    label: String = stringResource(Res.string.educational_stage),
    onLoadNextStages: () -> Unit = {},
    isStageLoading: Boolean = false,
    isStageLoadFailed: Boolean = false,
    onRetryLoadStages: () -> Unit = {},
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalStageSelectContent(
        displayValue = selectedStages.joinToString(", ", transform = itemTitle),
        label = label,
        educationalStages = educationalStages,
        itemTitle = itemTitle,
        itemId = itemId,
        isMultiSelect = true,
        isSelected = isSelected,
        isNoneSelected = false,
        allowClear = false,
        isSheetVisible = isSheetVisible,
        onToggleSheet = onToggleSheet,
        onSelectStage = { stage -> if (stage != null) onSelectStage(stage) },
        onLoadNextStages = onLoadNextStages,
        isStageLoading = isStageLoading,
        isStageLoadFailed = isStageLoadFailed,
        onRetryLoadStages = onRetryLoadStages,
        errorText = errorText,
        modifier = modifier
    )
}

@Composable
private fun <T> EducationalStageSelectContent(
    displayValue: String,
    label: String,
    educationalStages: List<T>,
    itemTitle: (T) -> String,
    itemId: (T) -> Any,
    isMultiSelect: Boolean,
    isSelected: (T) -> Boolean,
    isNoneSelected: Boolean,
    allowClear: Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (T?) -> Unit,
    onLoadNextStages: () -> Unit,
    isStageLoading: Boolean,
    isStageLoadFailed: Boolean,
    onRetryLoadStages: () -> Unit,
    errorText: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        CustomTextField(
            value = displayValue,
            onValueChange = {},
            labelText = label,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onToggleSheet(true) },
            readOnly = true,
            enabled = false,
            errorText = errorText,
            trailingIcon = painterResource(Res.drawable.ic_chevron_down)
        )

        BottomSheet(
            isVisible = isSheetVisible,
            onDismiss = { onToggleSheet(false) }
        ) {
            val stageListState = rememberLazyListState()

            Text(
                text = label,
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            LookupContentContainer(
                isLoading = isStageLoading,
                isError = isStageLoadFailed,
                isEmpty = educationalStages.isEmpty() && !allowClear,
                errorMessage = stringResource(Res.string.failed_to_load_educational_stages),
                onRetry = onRetryLoadStages
            ) {
                LazyColumn(
                    state = stageListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = educationalStages,
                        key = { stage -> itemId(stage) }
                    ) { stage ->
                        val selected = isSelected(stage)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableNoRipple {
                                    onSelectStage(stage)
                                    if (!isMultiSelect) {
                                        onToggleSheet(false)
                                    }
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (isMultiSelect) {
                                Checkbox(
                                    checked = selected,
                                    onCheckedChange = { onSelectStage(stage) },
                                    checkedColor = Theme.colorScheme.primary,
                                    uncheckedColor = Theme.colorScheme.outline
                                )
                            }
                            Text(
                                text = itemTitle(stage),
                                style = Theme.typography.bodyLarge,
                                color = if (!isMultiSelect && selected) Theme.colorScheme.primary else Theme.colorScheme.onSurface
                            )
                        }
                    }

                    if (allowClear) {
                        item(key = "no_stage_option") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableNoRipple {
                                        onSelectStage(null)
                                        if (!isMultiSelect) {
                                            onToggleSheet(false)
                                        }
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.no_stage),
                                    style = Theme.typography.bodyLarge,
                                    color = if (isNoneSelected) Theme.colorScheme.primary else Theme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                PaginationTrigger(
                    list = educationalStages,
                    listState = stageListState,
                    remainingItemsToLoadNextPage = 5,
                    loadNextItems = onLoadNextStages
                )

                if (isMultiSelect) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        type = AppButtonType.Primary,
                        onClick = { onToggleSheet(false) },
                        text = stringResource(Res.string.ok),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EducationalStageSelectFieldPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            EducationalStageSelectField<String>(
                selectedStage = null,
                educationalStages = emptyList(),
                itemTitle = { it },
                itemId = { it },
                isSelected = { false },
                isSheetVisible = false,
                onToggleSheet = {},
                onSelectStage = {}
            )
        }
    }
}
