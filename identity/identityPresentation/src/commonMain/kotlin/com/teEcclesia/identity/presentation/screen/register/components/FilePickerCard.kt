package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.OutlinedTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.dashedBorder
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_folder
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.supported_formats

@Composable
fun FilePickerCard(
    title: String,
    fileName: String?,
    onUploadClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    radius: Dp = 32.dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = fileName.isNullOrBlank(),
            transitionSpec = {
                (fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.95f)) togetherWith
                        (fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.95f))
            },
            label = "FilePickerStateTransition"
        ) { isEmpty ->
            if (isEmpty) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dashedBorder(
                            width = 1.dp,
                            color = Theme.colorScheme.outline,
                            shape = RoundedCornerShape(radius),
                            dashLength = 8.dp,
                            gapLength = 4.dp
                        )
                        .padding(8.dp)
                        .background(Theme.colorScheme.secondary, RoundedCornerShape(radius - 8.dp))
                        .clickableNoRipple(onClick = onUploadClick)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_folder),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSecondary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title,
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSecondary
                    )
                    Text(
                        text = stringResource(Res.string.supported_formats),
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSecondary
                    )
                }
            } else {
                OutlinedTextField(
                    value = fileName.orEmpty(),
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Clear file",
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clickableNoRipple(onClick = onClearClick)
                                .padding(4.dp)
                        )
                    },
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun FilePickerCardPreview() {
    Theme {
        FilePickerCard(
            title = "Upload your document",
            fileName = null,
            onUploadClick = {},
            onClearClick = {}
        )
    }
}

@PreviewLightDark
@Composable
fun FilePickerCardWithFilePreview() {
    Theme {
        FilePickerCard(
            title = "Upload your document",
            fileName = "document.pdf",
            onUploadClick = {},
            onClearClick = {}
        )
    }
}