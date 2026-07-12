package com.teEcclesia.designsystem.components.icon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import androidx.compose.ui.tooling.preview.Preview
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_home

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Theme.colorScheme.primary.variant600,
        containerColor = Color.Unspecified
    ),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = BorderStroke(
        width = 0.5.dp,
        color = Theme.colorScheme.border.active
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
@Preview
fun OutlinedButtonPreview() = TeEcclesiaTheme {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.padding(16.dp).size(56.dp),
        enabled = true,
        shape = ButtonDefaults.outlinedShape,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            painter = Res.drawable.ic_home.painter(),
            contentDescription = null,
            tint = Theme.colorScheme.primary.variant600,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
@Preview
fun OutlinedButtonPreview2() = TeEcclesiaTheme {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.padding(16.dp).height(56.dp),
        enabled = true,
        shape = ButtonDefaults.outlinedShape,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            "Button Button Button",
            style = Theme.typography.label.medium.medium,
        )
    }
}
