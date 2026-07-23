package com.teEcclesia.designsystem.modifier

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    shape: Shape = CircleShape,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 4.dp
) = drawWithCache {
    val strokeWidthPx = width.toPx()
    val dashPx = dashLength.toPx()
    val gapPx = gapLength.toPx()

    val stroke = Stroke(
        width = strokeWidthPx,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)
    )
    val outline = shape.createOutline(size, layoutDirection, density = this)

    onDrawBehind {
        drawOutline(
            outline = outline,
            color = color,
            style = stroke
        )
    }
}