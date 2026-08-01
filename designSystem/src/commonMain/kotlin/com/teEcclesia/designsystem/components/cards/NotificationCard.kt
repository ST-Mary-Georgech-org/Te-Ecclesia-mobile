package com.teEcclesia.designsystem.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_bell

@Composable
fun NotificationCard(
    icon: DrawableResource,
    title: String,
    description: String,
    current: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Theme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Column(
        modifier = modifier
            .background(backgroundColor, shape)
            .border(1.dp, Theme.colorScheme.outlineVariant, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = Theme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(start = 8.dp).weight(1f),
                text = title,
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSurface,
                maxLines = 1,
                overflow = Ellipsis,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = current,
                style = Theme.typography.bodySmall,
                color = Theme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            modifier = Modifier,
            text = description,
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun NotificationCardPreview() {
    Theme {
        NotificationCard(
            icon = Res.drawable.ic_bell,
            title = "إشعار جديد",
            current = "1h",
            description = "تم استقبال طلب جديد متاح للمراجعة",
        )
    }
}
