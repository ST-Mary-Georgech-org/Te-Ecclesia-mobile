package com.teEcclesia.designsystem.components.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OtpInputField(
    otpText: String,
    modifier: Modifier = Modifier,
    otpLength: Int = 5,
    errorText: String? = null,
    shouldShowCursor: Boolean = true,
    shouldCursorBlink: Boolean = true,
    backgroundColor: Color = Color.Transparent,
    spaceBetweenCharacters: Dp = 12.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    onOtpModified: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpLength) {
            onOtpModified(otpText.take(otpLength))
        }
    }

    Column(modifier = modifier) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            BasicTextField(
                modifier = Modifier.height(64.dp),
                value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
                onValueChange = {
                    if (it.text.length <= otpLength && it.text.all { char -> char.isDigit() }) {
                        onOtpModified.invoke(it.text)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spaceBetweenCharacters),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(otpLength) { index ->
                            CharacterContainer(
                                index = index,
                                text = otpText,
                                isError = errorText.isNullOrBlank().not(),
                                shape = shape,
                                backgroundColor = backgroundColor,
                                shouldShowCursor = shouldShowCursor,
                                shouldCursorBlink = shouldCursorBlink,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            )
        }

        // Animated spacer
        AnimatedVisibility(
            visible = !errorText.isNullOrBlank(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Error text
        AnimatedVisibility(
            visible = !errorText.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorText ?: "",
                color = Theme.colorScheme.additional.onError,
                modifier = Modifier.padding(start = 4.dp),
                style = Theme.typography.body.medium,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
internal fun CharacterContainer(
    index: Int,
    text: String,
    isError: Boolean,
    shape: Shape,
    backgroundColor: Color,
    shouldShowCursor: Boolean,
    shouldCursorBlink: Boolean,
    modifier: Modifier = Modifier
) {
    val isFocused = text.length == index
    val character = when {
        index < text.length -> text[index].toString()
        else -> ""
    }

    // Cursor visibility state
    val cursorVisible = remember { mutableStateOf(shouldShowCursor) }

    // Blinking effect for the cursor
    LaunchedEffect(key1 = isFocused) {
        if (isFocused && shouldShowCursor && shouldCursorBlink) {
            while (true) {
                delay(800)
                cursorVisible.value = !cursorVisible.value
            }
        } else if (!isFocused) {
            cursorVisible.value = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(64.dp)
            .border(
                width = 1.dp,
                color = when {
                    isError -> Theme.colorScheme.additional.onError
                    isFocused -> Theme.colorScheme.primary.variant600
                    character.isNotEmpty() -> Theme.colorScheme.border.tertiary
                    else -> Theme.colorScheme.border.secondary
                },
                shape = shape
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
    ) {
        Text(
            text = character,
            style = Theme.typography.title.large,
            color = Theme.colorScheme.primary.variant800,
            textAlign = TextAlign.Center
        )

        // Display cursor when focused
        AnimatedVisibility(
            visible = isFocused && cursorVisible.value && character.isEmpty()
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(24.dp)
                    .background(Theme.colorScheme.primary.variant600)
            )
        }
    }
}

@Composable
@Preview
fun OtpInputFieldPreview() = TeEcclesiaTheme {
    OtpInputField(
        otpText = "12345",
        otpLength = 5,
        onOtpModified = {}
    )
}
