package com.teEcclesia.designsystem.components.dialog

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.ok

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    showDialog: Boolean,
    selectedDate: LocalDate?,
    maxDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = stringResource(Res.string.ok),
    dismissText: String = stringResource(Res.string.cancel),
    containerColor: Color = Theme.colorScheme.background,
    contentColor: Color = Theme.colorScheme.onSurface,
    brandColor: Color = Theme.colorScheme.primary,
    errorColor: Color = Theme.colorScheme.error,
) {
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = (selectedDate ?: maxDate)?.toEpochDays()?.let { it * 86400000 },
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val maxDateMillis = maxDate?.toEpochDays()?.let { it * 86400000 }
                    return if (maxDateMillis != null) {
                        utcTimeMillis <= maxDateMillis
                    } else {
                        true
                    }
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return maxDate?.year?.let { year <= it } ?: true
                }
            }
        )

        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = brandColor),
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateSelected(LocalDate.fromEpochDays((it / 86400000).toInt()))
                            }
                            onDismiss()
                        }
                    ) { Text(confirmText) }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = brandColor),
                        onClick = onDismiss
                    ) { Text(dismissText) }
                },
                colors = DatePickerDefaults.colors(containerColor = containerColor)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true,
                    colors = DatePickerDefaults.colors(
                        containerColor = containerColor,
                        titleContentColor = contentColor,
                        headlineContentColor = Theme.colorScheme.onSurfaceVariant,
                        weekdayContentColor = Theme.colorScheme.onSurfaceVariant,
                        dayContentColor = Theme.colorScheme.onSurfaceVariant,
                        yearContentColor = Theme.colorScheme.onSurfaceVariant,
                        selectedDayContainerColor = brandColor,
                        selectedYearContainerColor = brandColor,
                        currentYearContentColor = brandColor,
                        dividerColor = Theme.colorScheme.outline,
                        todayContentColor = brandColor,
                        todayDateBorderColor = brandColor,
                        navigationContentColor = contentColor,
                        subheadContentColor = contentColor,
                        dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = brandColor,
                            unfocusedBorderColor = Theme.colorScheme.outline,
                            focusedLabelColor = brandColor,
                            unfocusedLabelColor = contentColor,
                            cursorColor = brandColor,
                            focusedTextColor = Theme.colorScheme.onSurfaceVariant,
                            unfocusedTextColor = contentColor,
                            focusedPlaceholderColor = contentColor,
                            unfocusedPlaceholderColor = contentColor,
                            errorTextColor = Theme.colorScheme.onSurfaceVariant,
                            errorLabelColor = errorColor,
                            errorCursorColor = errorColor,
                            errorBorderColor = errorColor,
                            errorTrailingIconColor = errorColor,
                            errorLeadingIconColor = errorColor,
                            errorPrefixColor = errorColor,
                            errorSuffixColor = errorColor,
                            errorPlaceholderColor = errorColor,
                            errorSupportingTextColor = errorColor
                        )
                    ),
                )
            }
        }
    }
}

@Composable
@Preview
fun DatePickerDialogPreview() = Theme {
    DatePicker(
        showDialog = true,
        selectedDate = LocalDate(2026, 1, 7),
        onDateSelected = {},
        onDismiss = {}
    )
}
