package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.UserSummary
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_close

@Composable
fun UserChip(
    user: UserSummary,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Theme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!user.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = user.name,
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSurface
            )
        }
        Icon(
            painter = painterResource(Res.drawable.ic_close),
            contentDescription = "Remove user",
            tint = Theme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clickableNoRipple(onClick = onRemove)
                .padding(8.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun UserChipPreview() = Theme {
    UserChip(
        user = UserSummary(
            id = "1",
            name = "John Doe",
            imageUrl = null,
            code = "123456"
        ),
        onRemove = {}
    )
}