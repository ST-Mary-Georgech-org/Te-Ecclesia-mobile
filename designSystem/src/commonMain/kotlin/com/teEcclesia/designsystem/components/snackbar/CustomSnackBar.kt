package com.teEcclesia.designsystem.components.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import com.teEcclesia.designsystem.utils.PreviewMultiDevices
import com.teEcclesia.designsystem.utils.TeEcclesiaPreview
import com.teEcclesia.designsystem.utils.customShadow
import kotlinx.coroutines.delay
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_error
import teecclesia.designsystem.generated.resources.ic_success

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    data: SnackBarData,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    onDismiss: () -> Unit = {},
) {
    Box(
        modifier.fillMaxWidth()
            .dropShadow(shape = shape, shadow = customShadow)
            .clip(shape)
            .clickableNoRipple(onClick = onDismiss)
    ) {
        Row(
            modifier = Modifier
                .clip(shape)
                .background(Theme.colorScheme.background.tertiary)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = data.customLeadingIcon ?: when {
                    data.isSuccess -> Res.drawable.ic_success
                    else -> Res.drawable.ic_error
                }.painter(),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = data.iconTint
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.title,
                    style = Theme.typography.heading.extraSmall,
                    color = Theme.colorScheme.text.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                data.message?.let {
                    Text(
                        text = data.message,
                        style = Theme.typography.body.small,
                        color = Theme.colorScheme.text.titleSmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedSnackBar(
    isVisible: Boolean,
    data: SnackBarData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier.fillMaxWidth()
    ) {
        LaunchedEffect(data) {
            delay(data.duration ?: 2500L)
            onDismiss()
        }
        CustomSnackBar(
            modifier = Modifier,
            data = data,
            onDismiss = onDismiss
        )
    }
}

data class SnackBarData(
    val title: String,
    val message: String? = null,
    val isSuccess: Boolean = true,
    val customLeadingIcon: Painter? = null,
    val duration: Long? = null,
    val iconTint: Color = Color.Unspecified
)

@Composable
@PreviewMultiDevices
fun CustomSnackBarPreview() = TeEcclesiaPreview {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSnackBar(
            data = SnackBarData(
                title = "Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title Title",
                message = "Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message Message",
                ////        leadingIcon = painterResource(Res.drawable.ic_home),
            )
        )
        CustomSnackBar(
            data = SnackBarData(
                title = "Title Title Title Title ",
                message = "Message Message Message Message ",
                isSuccess = false,
            )
        )
    }
}