package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.area
import teecclesia.designsystem.generated.resources.failed_to_load_areas

@Composable
fun AreaDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    areas: List<String>,
    isAreaSheetVisible: Boolean,
    onToggleAreaSheet: (Boolean) -> Unit,
    onSelectArea: (String) -> Unit,
    errorText: String?,
    isAreaLoading: Boolean = false,
    isAreaLoadFailed: Boolean = false,
    onRetryLoadAreas: () -> Unit = {},
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next
) {
    Box(modifier = modifier.fillMaxWidth()) {
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            labelText = stringResource(Res.string.area),
            modifier = Modifier.fillMaxWidth(),
            errorText = errorText,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = imeAction),
        )

        DropdownMenu(
            expanded = isAreaSheetVisible && (areas.isNotEmpty() || isAreaLoading || isAreaLoadFailed),
            onDismissRequest = { onToggleAreaSheet(false) },
            properties = PopupProperties(focusable = false),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            LookupContentContainer(
                isLoading = isAreaLoading,
                isError = isAreaLoadFailed,
                isEmpty = areas.isEmpty(),
                errorMessage = stringResource(Res.string.failed_to_load_areas),
                onRetry = onRetryLoadAreas
            ) {
                areas.forEach { areaItem ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = areaItem,
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onSelectArea(areaItem)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AreaDropdownFieldPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            AreaDropdownField(
                value = "Zamalek",
                onValueChange = {},
                areas = listOf("Zamalek", "Maadi"),
                isAreaSheetVisible = false,
                onToggleAreaSheet = {},
                onSelectArea = {},
                errorText = null
            )
        }
    }
}
