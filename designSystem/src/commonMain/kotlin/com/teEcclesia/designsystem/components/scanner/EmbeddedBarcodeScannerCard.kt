package com.teEcclesia.designsystem.components.scanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import qrscanner.CameraLens
import qrscanner.OverlayShape
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.position_the_qr_code_within_the_frame_to_scan
import teecclesia.designsystem.generated.resources.scan_qr_code

@Composable
fun EmbeddedBarcodeScannerCard(
    isVisible: Boolean,
    onClose: () -> Unit,
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        val cardShape = RoundedCornerShape(16.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colorScheme.surface, cardShape)
                .border(1.dp, Theme.colorScheme.outlineVariant, cardShape)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(Res.string.scan_qr_code),
                            style = Theme.typography.titleMedium,
                            color = Theme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(Res.string.position_the_qr_code_within_the_frame_to_scan),
                            style = Theme.typography.bodySmall,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Theme.colorScheme.surfaceVariant)
                            .clickableNoRipple { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Close scanner",
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                var lastScannedCode by remember { mutableStateOf("") }
                var lastScanTimeMark by remember { mutableStateOf<TimeMark?>(null) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Theme.colorScheme.surfaceVariant)
                        .border(1.dp, Theme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                ) {
                    QrScanner(
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        flashlightOn = false,
                        cameraLens = CameraLens.Back,
                        overlayShape = OverlayShape.Square,
                        overlayColor = Color.Transparent,
                        overlayBorderColor = Color.Transparent,
                        customOverlay = {},
                        permissionDeniedView = { onOpenSettings ->
                            EmbeddedCameraPermissionDeniedContent(
                                onOpenSettings = onOpenSettings
                            )
                        },
                        onCompletion = { result ->
                            val trimmed = result.trim()
                            if (trimmed.isNotBlank()) {
                                val mark = lastScanTimeMark
                                val shouldScan = trimmed != lastScannedCode || mark == null || mark.elapsedNow() > 1500.milliseconds
                                if (shouldScan) {
                                    lastScannedCode = trimmed
                                    lastScanTimeMark = TimeSource.Monotonic.markNow()
                                    onQrCodeScanned(trimmed)
                                }
                            }
                        },
                        onFailure = {}
                    )
                }
            }
        }
    }
}
