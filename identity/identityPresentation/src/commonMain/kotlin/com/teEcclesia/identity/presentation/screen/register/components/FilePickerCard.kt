package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.dashedBorder
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_folder
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.supported_formats

import com.teEcclesia.identity.presentation.util.getDisplayFileName

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically

@Composable
fun FilePickerCard(
    title: String,
    fileName: String?,
    onUploadClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    fileTitle: String? = null,
    radius: Dp = 32.dp,
    onFileClick: (() -> Unit)? = null,
    errorText: String? = null
) {
    val displayFileName = getDisplayFileName(fileTitle, fileName)

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
                            color = if (!errorText.isNullOrBlank()) Theme.colorScheme.error else Theme.colorScheme.outline,
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
                        color = Theme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(Res.string.supported_formats),
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSecondary
                    )
                }
            } else {
                CustomTextField(
                    value = displayFileName,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple { onFileClick?.invoke() },
                    trailingIcon = painterResource(Res.drawable.ic_close),
                    onTrailingIconClick = onClearClick,
                    errorText = errorText
                )
            }
        }

        AnimatedVisibility(
            visible = fileName.isNullOrBlank() && !errorText.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText ?: "",
                    color = Theme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp),
                    style = Theme.typography.bodySmall,
                    textAlign = TextAlign.Start
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