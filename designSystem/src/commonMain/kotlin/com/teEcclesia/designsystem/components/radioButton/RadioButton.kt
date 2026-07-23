package com.teEcclesia.designsystem.components.radioButton

import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teEcclesia.designsystem.theme.theme.Theme
import androidx.compose.material3.RadioButton as M3RadioButton

@Composable
fun RadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Theme.colorScheme.primary,
    unselectedColor: Color = Theme.colorScheme.onSurfaceVariant
) {
    M3RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(
            selectedColor = selectedColor,
            unselectedColor = unselectedColor
        )
    )
}
