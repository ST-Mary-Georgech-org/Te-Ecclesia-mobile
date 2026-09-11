package com.teEcclesia.identity.presentation.screen.attendance.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.attendance.register.AttendanceRegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.attendance.register.AttendanceRegisterUiState
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm_remove_attendee
import teecclesia.designsystem.generated.resources.delete

@Composable
fun RemoveAttendeeConfirmSheet(
    state: AttendanceRegisterUiState,
    listener: AttendanceRegisterInteractionListener,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = state.isRemoveConfirmSheetOpen,
        onDismiss = listener::onDismissSheet
    ) {
        RemoveAttendeeConfirmSheetContent(
            isActionLoading = state.isActionLoading,
            onDismiss = listener::onDismissSheet,
            onConfirm = listener::onConfirmRemoveAttendee,
            modifier = modifier
        )
    }
}

@Composable
fun RemoveAttendeeConfirmSheetContent(
    isActionLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.confirm_remove_attendee),
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
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel)
            )

            AppButton(
                type = AppButtonType.Primary,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.delete),
                state = if (isActionLoading) AppButtonState.Loading else AppButtonState.Enabled,
                enablePrimaryBackgroundColor = Theme.colorScheme.error
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun RemoveAttendeeConfirmSheetPreview() = Theme {
    RemoveAttendeeConfirmSheetContent(
        isActionLoading = false,
        onDismiss = {},
        onConfirm = {}
    )
}
