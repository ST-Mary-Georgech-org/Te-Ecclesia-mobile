package com.teEcclesia.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.topBorder(
    strokeWidth: Dp = 1.dp,
    color: Color
) = this.drawBehind {
    val strokeWidthPx = strokeWidth.toPx()
    drawLine(
        color = color,
        start = Offset(x = 0f, y = strokeWidthPx / 2),
        end = Offset(x = size.width, y = strokeWidthPx / 2),
        strokeWidth = strokeWidthPx
    )
}