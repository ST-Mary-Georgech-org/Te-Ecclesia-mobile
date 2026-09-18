package com.teEcclesia.designsystem.components.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonSize
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.camera_permission_message
import teecclesia.designsystem.generated.resources.ic_qr_scan
import teecclesia.designsystem.generated.resources.permission_required
import teecclesia.designsystem.generated.resources.settings

@Composable
fun EmbeddedCameraPermissionDeniedContent(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_qr_scan),
                    contentDescription = null,
                    tint = Theme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )
            }

            Text(
                text = stringResource(Res.string.permission_required),
                style = Theme.typography.titleSmall,
                color = Theme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(Res.string.camera_permission_message),
                style = Theme.typography.bodySmall,
                color = Theme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(2.dp))

            AppButton(
                text = stringResource(Res.string.settings),
                onClick = onOpenSettings,
                type = AppButtonType.Primary,
                state = AppButtonState.Enabled,
                size = AppButtonSize.Small
            )
        }
    }
}
