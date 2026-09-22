package com.teEcclesia.designsystem.components.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun AppSwipeToDismissBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thresholdDp: Dp = 90.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundContent: @Composable (progress: Float) -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val thresholdPx = with(density) { thresholdDp.toPx() }
    val offsetX = remember { Animatable(0f) }
    var hasVibrated by remember { mutableStateOf(false) }

    val logicalOffset = if (isRtl) offsetX.value else -offsetX.value
    val progress = (logicalOffset / thresholdPx).coerceIn(0f, 1f)
    val contentAlpha = (1f - progress * 0.7f).coerceIn(0.3f, 1f)

    Box(
        modifier = modifier
            .clip(shape)
            .pointerInput(enabled, isRtl) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures(
                    onDragStart = {
                        hasVibrated = false
                    },
                    onDragEnd = {
                        hasVibrated = false
                        val currentLogicalOffset = if (isRtl) offsetX.value else -offsetX.value
                        if (currentLogicalOffset >= thresholdPx) {
                            coroutineScope.launch {
                                val targetDismissOffset = if (isRtl) 1000f else -1000f
                                offsetX.animateTo(
                                    targetValue = targetDismissOffset,
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                )
                                onDismiss()
                            }
                        } else {
                            coroutineScope.launch {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        hasVibrated = false
                        coroutineScope.launch {
                            offsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newOffset = offsetX.value + dragAmount
                            val newLogical = if (isRtl) newOffset else -newOffset

                            if (newLogical >= 0f) {
                                offsetX.snapTo(newOffset)
                                val isPastThreshold = newLogical >= thresholdPx
                                if (isPastThreshold && !hasVibrated) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    hasVibrated = true
                                } else if (!isPastThreshold && hasVibrated) {
                                    hasVibrated = false
                                }
                            }
                        }
                    }
                )
            }
    ) {
        if (progress > 0f) {
            Box(modifier = Modifier.matchParentSize()) {
                backgroundContent(progress)
            }
        }

        Box(
            modifier = Modifier.graphicsLayer {
                translationX = offsetX.value
                alpha = contentAlpha
            }
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun AppSwipeToDismissBoxPreview() {
    Theme {
        AppSwipeToDismissBox(
            onDismiss = {},
            backgroundContent = {
                Text(
                    text = "Delete",
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.error
                )
            }
        ) {
            Text(
                text = "Swipe Me",
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onBackground
            )
        }
    }
}
