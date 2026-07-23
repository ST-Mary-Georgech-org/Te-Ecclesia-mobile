package com.teEcclesia.designsystem.modifier

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.teEcclesia.designsystem.theme.theme.Theme

@Composable
fun Modifier.shimmerEffect(
    colors: List<Color> = listOf(
        Theme.colorScheme.outline,
        Theme.colorScheme.outline.copy(alpha = 0.2f),
        Theme.colorScheme.outline,
    ),
    durationMillis: Int = 600,
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")

    val animatedOffset by transition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
        ),
        label = "offset"
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(animatedOffset * size.width, 0f),
            end = Offset(animatedOffset * size.width + size.width, size.height.toFloat()),
        )
    ).onGloballyPositioned { size = it.size }
}
