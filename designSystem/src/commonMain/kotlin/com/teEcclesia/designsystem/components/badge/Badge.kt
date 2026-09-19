package com.teEcclesia.designsystem.components.badge

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.preview.PreviewThemes

@Composable
fun Badge(
    modifier: Modifier = Modifier,
    containerColor: Color = Theme.colorScheme.error,
    contentColor: Color = Theme.colorScheme.onError,
    content: @Composable (RowScope.() -> Unit)? = null
) {
    androidx.compose.material3.Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}

@PreviewLightDark
@Composable
private fun BadgePreview() = Theme {
    Badge {
        Text(
            text = "5",
            style = Theme.typography.labelSmall,
            color = Theme.colorScheme.onError
        )
    }
}
