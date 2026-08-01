package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.dashedBorder
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_document
import teecclesia.designsystem.generated.resources.replace

import com.teEcclesia.identity.presentation.util.getDisplayFileName

@Composable
fun ReviewFilePickerRow(
    label: String,
    fileName: String?,
    onUploadClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    fileTitle: String? = null,
    onFileClick: (() -> Unit)? = null
) {
    val displayFileName = getDisplayFileName(fileTitle, fileName)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!fileName.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Theme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickableNoRipple { onFileClick?.invoke() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = displayFileName,
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Theme.colorScheme.surfaceContainerHigh)
                            .clickableNoRipple(onClick = onClearClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .dashedBorder(
                        width = 1.dp,
                        color = Theme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp),
                        dashLength = 4.dp,
                        gapLength = 3.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickableNoRipple(onClick = onUploadClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_document),
                    contentDescription = null,
                    tint = Theme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(Res.string.replace),
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.primary
                )
            }
        }
    }
}
