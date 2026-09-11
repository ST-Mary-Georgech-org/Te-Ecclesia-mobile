package com.teEcclesia.identity.presentation.shared.components

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
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ok

/**
 * Single-select Educational Stage field overload.
 */
@Composable
fun EducationalStageSelectField(
    selectedStage: LookupResponse?,
    educationalStages: List<LookupResponse>,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (LookupResponse) -> Unit,
    label: String = stringResource(Res.string.educational_stage),
    onLoadNextStages: () -> Unit = {},
    isStageLoading: Boolean = false,
    isStageLoadFailed: Boolean = false,
    onRetryLoadStages: () -> Unit = {},
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalStageSelectContent(
        displayValue = selectedStage?.name ?: "",
        label = label,
        educationalStages = educationalStages,
        isMultiSelect = false,
        isSelected = { stage -> selectedStage?.id == stage.id },
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
 * Multi-select Educational Stage field overload.
 */
@Composable
fun EducationalStageSelectField(
    selectedStages: List<LookupResponse>,
    educationalStages: List<LookupResponse>,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (LookupResponse) -> Unit,
    label: String = stringResource(Res.string.educational_stage),
    onLoadNextStages: () -> Unit = {},
    isStageLoading: Boolean = false,
    isStageLoadFailed: Boolean = false,
    onRetryLoadStages: () -> Unit = {},
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalStageSelectContent(
        displayValue = selectedStages.joinToString(", ") { it.name },
        label = label,
        educationalStages = educationalStages,
        isMultiSelect = true,
        isSelected = { stage -> selectedStages.any { it.id == stage.id } },
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

@Composable
private fun EducationalStageSelectContent(
    displayValue: String,
    label: String,
    educationalStages: List<LookupResponse>,
    isMultiSelect: Boolean,
    isSelected: (LookupResponse) -> Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectStage: (LookupResponse) -> Unit,
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
                isEmpty = educationalStages.isEmpty(),
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
                        key = { stage -> stage.id }
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
                                text = stage.name,
                                style = Theme.typography.bodyLarge,
                                color = Theme.colorScheme.onSurface
                            )
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
            EducationalStageSelectField(
                selectedStage = null,
                educationalStages = emptyList(),
                isSheetVisible = false,
                onToggleSheet = {},
                onSelectStage = {}
            )
        }
    }
}
