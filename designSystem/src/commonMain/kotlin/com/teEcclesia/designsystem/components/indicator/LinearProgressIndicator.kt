package com.teEcclesia.designsystem.components.indicator

import androidx.compose.material3.LinearProgressIndicator as M3LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.teEcclesia.designsystem.theme.theme.Theme

@Composable
fun LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.primary,
    trackColor: Color = Theme.colorScheme.surfaceContainerHigh,
    strokeCap: StrokeCap = StrokeCap.Round
) {
    M3LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap,
        drawStopIndicator = {}
    )
}
