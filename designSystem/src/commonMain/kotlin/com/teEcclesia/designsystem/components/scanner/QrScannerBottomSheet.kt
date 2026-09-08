package com.teEcclesia.designsystem.components.scanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import org.jetbrains.compose.resources.stringResource
import qrscanner.CameraLens
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.position_the_qr_code_within_the_frame_to_scan
import teecclesia.designsystem.generated.resources.scan_qr_code

@Composable
fun QrScannerBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismissRequest,
        containerColor = Theme.colorScheme.surface,
        horizontalPadding = 24.dp
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.scan_qr_code),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.position_the_qr_code_within_the_frame_to_scan),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            var lastScannedCode by remember { mutableStateOf("") }
            var lastScanTimeMark by remember { mutableStateOf<TimeMark?>(null) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Theme.colorScheme.surfaceVariant)
            ) {
                QrScanner(
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    flashlightOn = false,
                    cameraLens = CameraLens.Back,
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
