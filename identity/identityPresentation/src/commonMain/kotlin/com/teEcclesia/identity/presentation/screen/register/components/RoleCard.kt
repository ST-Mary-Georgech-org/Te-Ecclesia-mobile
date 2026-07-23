package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.thenIf
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.TeEcclesiaPreview
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_servant

@Composable
fun RoleCard(
    title: String,
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .thenIf(isSelected) {
                border(
                    width = 1.dp,
                    color = Theme.colorScheme.outline,
                    shape = RoundedCornerShape(24.dp)
                )
            }
            .clickableNoRipple(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 100.dp)
                .background(
                    if (Theme.isDarkTheme) Theme.colorScheme.onSurfaceVariant else Theme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.height(100.dp)
            )
        }
        Text(
            text = title,
            style = Theme.typography.labelMedium,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
@PreviewLightDark
fun RoleCardPreview() = Theme {
    TeEcclesiaPreview(darkTheme = Theme.isDarkTheme) {
        RoleCard(
            title = "Khadem",
            icon = painterResource(Res.drawable.ic_servant),
            isSelected = true,
            onClick = {}
        )
    }
}