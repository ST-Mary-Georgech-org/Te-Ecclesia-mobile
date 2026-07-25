package com.teEcclesia.designsystem.components.chips

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview

@Composable
fun SuggestionChip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = Theme.colorScheme.outlineVariant,
    textColor: Color = Theme.colorScheme.onPrimaryContainer,
    elevation: ChipElevation? = SuggestionChipDefaults.suggestionChipElevation(),
    borderColor: Color = Theme.colorScheme.primary,
    interactionSource: MutableInteractionSource? = null,
) {
    androidx.compose.material3.SuggestionChip(
        onClick = onClick,
        label = label,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        shape = shape,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = backgroundColor,
            labelColor = textColor
        ),
        elevation = elevation,
        border = SuggestionChipDefaults.suggestionChipBorder(
            enabled,
            borderColor = borderColor
        ),
        interactionSource = interactionSource
    )
}

@Composable
@PreviewLightDark
private fun SuggestionChipPreview() = Theme {
    Preview {
        SuggestionChip(
            onClick = {},
            label = { Text(text = "Suggestion Chip") }
        )
    }
}