package com.teEcclesia.identity.presentation.screen.attendance.services.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListInteractionListener
import com.teEcclesia.identity.presentation.screen.attendance.services.ServicesListUiState
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm_delete_service
import teecclesia.designsystem.generated.resources.delete

@Composable
fun DeleteServiceSheet(
    state: ServicesListUiState,
    listener: ServicesListInteractionListener
) {
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
