package com.teEcclesia.designsystem.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview

@Composable
fun SuggestionChip(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = Theme.colorScheme.outlineVariant,
    textColor: Color = Theme.colorScheme.onPrimaryContainer,
    borderColor: Color = Theme.colorScheme.primary,
    borderWidth: Dp = 1.dp,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 32.dp)
            .clip(shape)
            .background(backgroundColor, shape)
            .border(borderWidth, borderColor, shape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides textColor) {
            ProvideTextStyle(value = Theme.typography.labelLarge) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (icon != null) {
                        icon()
                    }
                    label()
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun SuggestionChipPreview() = Theme {
    Preview {
        SuggestionChip(
            label = { Text(text = "Suggestion Chip") }
        )
    }
}