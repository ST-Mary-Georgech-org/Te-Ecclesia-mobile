package com.teEcclesia.designsystem.components.badge

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.preview.PreviewThemes
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_bell

@Composable
fun BadgedBox(
    badge: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    androidx.compose.material3.BadgedBox(
        badge = badge,
        modifier = modifier,
        content = content
    )
}

@PreviewLightDark
@Composable
private fun BadgedBoxPreview() = Theme {
    BadgedBox(
        badge = {
            Badge {
                Text(
                    text = "3",
                    style = Theme.typography.labelSmall,
                    color = Theme.colorScheme.onError
                )
            }
        }
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_bell),
            contentDescription = "Notifications",
            tint = Theme.colorScheme.onBackground
        )
    }
}
