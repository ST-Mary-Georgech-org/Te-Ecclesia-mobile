package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.dashedBorder
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_plus

@Composable
fun AvatarPicker(
    imageBytes: ByteArray?,
    imageUrl: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Theme.colorScheme.secondaryContainer)
                .dashedBorder(
                    width = 1.dp,
                    color = Theme.colorScheme.onBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val model: Any? = imageBytes ?: imageUrl?.ifBlank { null }
            if (model != null) {
                AsyncImage(
                    model = model,
                    contentDescription = "Selected avatar",
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    Modifier.size(100.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                        contentDescription = "Avatar placeholder",
                        tint = Theme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .offset(x = (-4).dp, y = (-4).dp)
                .background(Theme.colorScheme.secondary, CircleShape)
                .padding(2.dp)
        ){
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = "Plus icon",
                tint = Theme.colorScheme.onSecondary,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AvatarPickerPreview() = Theme {
    AvatarPicker(
        imageBytes = null,
        onClick = {}
    )
}