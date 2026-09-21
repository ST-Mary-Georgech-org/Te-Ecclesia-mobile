package com.teEcclesia.identity.presentation.screen.areaSuggestion.components

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
import com.teEcclesia.identity.presentation.screen.areaSuggestion.AreaSuggestionsSettingsInteractionListener
import com.teEcclesia.identity.presentation.screen.areaSuggestion.AreaSuggestionsSettingsUiState
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.deleta_area_confirmation_message
import teecclesia.designsystem.generated.resources.delete_area_confirmation_title

@Composable
fun DeleteAreaConfirmSheet(
    state: AreaSuggestionsSettingsUiState,
    listener: AreaSuggestionsSettingsInteractionListener
) {
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