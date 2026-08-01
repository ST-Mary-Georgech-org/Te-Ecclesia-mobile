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
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.educational_year
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ok

/**
 * Single-select Educational Year field overload.
 */
@Composable
fun EducationalYearSelectField(
    selectedYear: LookupResponse?,
    availableYears: List<LookupResponse>,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectYear: (LookupResponse) -> Unit,
    label: String = stringResource(Res.string.educational_year),
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalYearSelectContent(
        displayValue = selectedYear?.name ?: "",
        label = label,
        availableYears = availableYears,
        isMultiSelect = false,
        isSelected = { year -> selectedYear?.id == year.id },
        isSheetVisible = isSheetVisible,
        onToggleSheet = onToggleSheet,
        onSelectYear = onSelectYear,
        errorText = errorText,
        modifier = modifier
    )
}

/**
 * Multi-select Educational Year field overload.
 */
@Composable
fun EducationalYearSelectField(
    selectedYears: List<LookupResponse>,
    availableYears: List<LookupResponse>,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectYear: (LookupResponse) -> Unit,
    label: String = stringResource(Res.string.educational_year),
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    EducationalYearSelectContent(
        displayValue = selectedYears.joinToString(", ") { it.name },
        label = label,
        availableYears = availableYears,
        isMultiSelect = true,
        isSelected = { year -> selectedYears.any { it.id == year.id } },
        isSheetVisible = isSheetVisible,
        onToggleSheet = onToggleSheet,
        onSelectYear = onSelectYear,
        errorText = errorText,
        modifier = modifier
    )
}

@Composable
private fun EducationalYearSelectContent(
    displayValue: String,
    label: String,
    availableYears: List<LookupResponse>,
    isMultiSelect: Boolean,
    isSelected: (LookupResponse) -> Boolean,
    isSheetVisible: Boolean,
    onToggleSheet: (Boolean) -> Unit,
    onSelectYear: (LookupResponse) -> Unit,
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
            skipPartiallyExpanded = isMultiSelect,
            onDismiss = { onToggleSheet(false) }
        ) {
            val listState = rememberLazyListState()

            Text(
                text = label,
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = availableYears,
                    key = { year -> year.id }
                ) { year ->
                    val selected = isSelected(year)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableNoRipple {
                                onSelectYear(year)
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
                                onCheckedChange = { onSelectYear(year) },
                                checkedColor = Theme.colorScheme.primary,
                                uncheckedColor = Theme.colorScheme.outline
                            )
                        }
                        Text(
                            text = year.name,
                            style = Theme.typography.bodyLarge,
                            color = Theme.colorScheme.onSurface
                        )
                    }
                }
            }

            if (isMultiSelect) {
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(
                    type = AppButtonType.Primary,
                    onClick = { onToggleSheet(false) },
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.ok)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EducationalYearSelectFieldPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            EducationalYearSelectField(
                selectedYear = null,
                availableYears = emptyList(),
                isSheetVisible = false,
                onToggleSheet = {},
                onSelectYear = {}
            )
        }
    }
}
