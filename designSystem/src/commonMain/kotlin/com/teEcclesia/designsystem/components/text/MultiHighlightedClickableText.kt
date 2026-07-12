package com.teEcclesia.designsystem.components.text

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.teEcclesia.designsystem.theme.theme.Theme

sealed class TextSegment {
    data class Normal(val text: String) : TextSegment()
    data class Highlighted(
        val text: String,
        val onClick: () -> Unit
    ) : TextSegment()
}

@Composable
fun MultiHighlightedClickableText(
    segments: List<TextSegment>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Theme.typography.label.medium.small,
    normalColor: Color = Theme.colorScheme.text.title,
    highlightColor: Color = Theme.colorScheme.button.primary,
    underlineHighlight: Boolean = false,
    textAlign: TextAlign = TextAlign.Center,
) {
    val annotatedText = buildAnnotatedString {
        segments.forEach { segment ->
            when (segment) {
                is TextSegment.Normal -> {
                    withStyle(SpanStyle(color = normalColor)) {
                        append(segment.text)
                    }
                }

                is TextSegment.Highlighted -> {
                    pushLink(
                        LinkAnnotation.Clickable(
                            tag = "highlight_${segment.text}",
                            linkInteractionListener = { _ -> segment.onClick() }
                        )
                    )
                    withStyle(
                        style = SpanStyle(
                            color = highlightColor,
                            textDecoration = if (underlineHighlight) {
                                TextDecoration.Underline
                            } else {
                                TextDecoration.None
                            },
                            fontWeight = textStyle.fontWeight
                        )
                    ) {
                        append(segment.text)
                    }
                    pop()
                }
            }
        }
    }

    BasicText(
        text = annotatedText,
        style = textStyle.copy(textAlign = textAlign),
        modifier = modifier
    )
}
