package com.teEcclesia.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.pdf.PdfViewer
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.painterResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_close

@Composable
fun PdfViewerDialog(
    isVisible: Boolean,
    pdf: ByteArray?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible || pdf == null) return

    var scale by remember(pdf) { mutableStateOf(1f) }
    var offset by remember(pdf) { mutableStateOf(Offset.Zero) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(pdf) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale > 1.05f) {
                                    scale = 1f
                                    offset = Offset.Zero
                                } else {
                                    scale = 2.5f
                                }
                            }
                        )
                    }
                    .pointerInput(pdf) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
                            do {
                                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                val isMultiTouch = event.changes.size >= 2

                                if (isMultiTouch) {
                                    val zoomChange = event.calculateZoom()
                                    val panChange = event.calculatePan()
                                    val newScale = (scale * zoomChange).coerceIn(1f, 5f)
                                    scale = newScale

                                    if (newScale <= 1.05f) {
                                        scale = 1f
                                        offset = Offset.Zero
                                    } else {
                                        val maxOffsetX = (size.width * (newScale - 1f)) / 2f
                                        val maxOffsetY = (size.height * (newScale - 1f)) / 2f
                                        offset = Offset(
                                            x = (offset.x + panChange.x).coerceIn(-maxOffsetX, maxOffsetX),
                                            y = (offset.y + panChange.y).coerceIn(-maxOffsetY, maxOffsetY)
                                        )
                                    }
                                    event.changes.forEach { it.consume() }
                                } else if (scale > 1.05f) {
                                    val panChange = event.calculatePan()
                                    val maxOffsetX = (size.width * (scale - 1f)) / 2f
                                    val maxOffsetY = (size.height * (scale - 1f)) / 2f
                                    offset = Offset(
                                        x = (offset.x + panChange.x).coerceIn(-maxOffsetX, maxOffsetX),
                                        y = (offset.y + panChange.y).coerceIn(-maxOffsetY, maxOffsetY)
                                    )
                                    event.changes.forEach { it.consume() }
                                }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
                    .padding(horizontal = 16.dp, vertical = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                PdfViewer(
                    pdf = pdf,
                    userScrollEnabled = scale <= 1.05f,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 16.dp, end = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickableNoRipple(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PdfViewerDialogPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            PdfViewerDialog(
                isVisible = true,
                pdf = null,
                onDismiss = {}
            )
        }
    }
}
