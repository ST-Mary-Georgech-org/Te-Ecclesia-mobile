package com.teEcclesia.identity.presentation.screen.areaSuggestion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.presentation.screen.areaSuggestion.AreaSuggestionsSettingsInteractionListener
import com.teEcclesia.identity.presentation.screen.areaSuggestion.AreaSuggestionsSettingsUiState
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_area
import teecclesia.designsystem.generated.resources.area
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.edit_area

@Composable
fun AddEditAreaSheet(
    state: AreaSuggestionsSettingsUiState,
    listener: AreaSuggestionsSettingsInteractionListener
) {
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
}