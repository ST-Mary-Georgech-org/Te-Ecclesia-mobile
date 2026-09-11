package com.teEcclesia.designsystem.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.ok

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    showDialog: Boolean,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    initialTime: LocalTime? = null,
    title: String? = null,
    is24Hour: Boolean = true,
    confirmText: String = stringResource(Res.string.ok),
    dismissText: String = stringResource(Res.string.cancel),
    containerColor: Color = Theme.colorScheme.surface,
    brandColor: Color = Theme.colorScheme.primary,
) {
    if (showDialog) {
        val timePickerState = rememberTimePickerState(
            initialHour = initialTime?.hour ?: 12,
            initialMinute = initialTime?.minute ?: 0,
            is24Hour = is24Hour
        )

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = containerColor,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            style = Theme.typography.labelMedium,
                            color = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        )
                    }

                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = Theme.colorScheme.surfaceVariant,
                            clockDialSelectedContentColor = Theme.colorScheme.onPrimary,
                            clockDialUnselectedContentColor = Theme.colorScheme.onSurface,
                            selectorColor = brandColor,
                            containerColor = containerColor,
                            periodSelectorBorderColor = Theme.colorScheme.outlineVariant,
                            periodSelectorSelectedContainerColor = Theme.colorScheme.primaryContainer,
                            periodSelectorUnselectedContainerColor = Color.Transparent,
                            periodSelectorSelectedContentColor = Theme.colorScheme.onPrimaryContainer,
                            periodSelectorUnselectedContentColor = Theme.colorScheme.onSurfaceVariant,
                            timeSelectorSelectedContainerColor = Theme.colorScheme.primaryContainer,
                            timeSelectorUnselectedContainerColor = Theme.colorScheme.surfaceVariant,
                            timeSelectorSelectedContentColor = Theme.colorScheme.onPrimaryContainer,
                            timeSelectorUnselectedContentColor = Theme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = brandColor),
                            onClick = onDismiss
                        ) {
                            Text(dismissText)
                        }

                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = brandColor),
                            onClick = {
                                onTimeSelected(LocalTime(timePickerState.hour, timePickerState.minute))
                                onDismiss()
                            }
                        ) {
                            Text(confirmText)
                        }
                    }
                }
            }
        }
    }
}
