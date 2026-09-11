package com.teEcclesia.designsystem.components.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.modifier.thenIf
import com.teEcclesia.designsystem.modifier.thenIfNotNull
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_eye_closed

private val EMOJI_REGEX = Regex("""[\u2600-\u27BF]|[\uD83C-\uDBFF][\uDC00-\uDFFF]|\p{So}|\p{Sk}""")


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = "",
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    trailingIconColor: Color? = null,
    isLoading: Boolean = false,
    backgroundColor: Color = Color.Unspecified,
    textColor: Color = Theme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = Theme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorText: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    allowEmojis: Boolean = false,
    onClick: (() -> Unit)? = null,
    showTrailingDivider: Boolean = false,
    prefixText: String? = null,
    suffixText: String? = null,
    maxChars: Int = if (singleLine) 150 else 1000,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    val colors = Theme.colorScheme
    val typography = Theme.typography
    val focusManager = LocalFocusManager.current

    val currentKeyboardActions by rememberUpdatedState(keyboardActions)

    val effectiveKeyboardActions = remember(focusManager) {
        KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                currentKeyboardActions.onDone?.invoke(this)
            },
            onSearch = {
                focusManager.clearFocus()
                currentKeyboardActions.onSearch?.invoke(this)
            },
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
                currentKeyboardActions.onNext?.invoke(this)
            },
            onGo = {
                focusManager.clearFocus()
                currentKeyboardActions.onGo?.invoke(this)
            },
            onSend = {
                focusManager.clearFocus()
                currentKeyboardActions.onSend?.invoke(this)
            }
        )
    }

    val interaction = remember { MutableInteractionSource() }
    val showError = !errorText.isNullOrBlank()

    val currentDirection = LocalLayoutDirection.current
    val isRtl = currentDirection == LayoutDirection.Rtl

    val animatedErrorColor by animateColorAsState(
        targetValue = if (showError) colors.error else colors.onSurfaceVariant,
        animationSpec = tween(durationMillis = 150),
        label = "TrailingIconColorAnimation"
    )
    val resolvedTrailingIconColor = trailingIconColor ?: animatedErrorColor

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor = colors.onSurfaceVariant,
        errorTextColor = textColor,
        focusedBorderColor = colors.outline,
        unfocusedBorderColor = colors.outlineVariant,
        disabledBorderColor = colors.outlineVariant,
        errorBorderColor = colors.error,
        focusedLabelColor = colors.outline,
        unfocusedLabelColor = colors.onSurfaceVariant,
        disabledLabelColor = colors.onSurfaceVariant,
        errorLabelColor = colors.error,
        cursorColor = colors.outline,
        errorCursorColor = colors.error,
        focusedContainerColor = backgroundColor,
        unfocusedContainerColor = backgroundColor,
        disabledContainerColor = backgroundColor,
        errorContainerColor = backgroundColor,
    )

    val rememberedPrefix = remember(prefixText, textStyle, textColor) {
        prefixText?.let { text ->
            @Composable {
                Text(
                    text = text,
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Start
                )
            }
        }
    }

    val rememberedSuffix = remember(suffixText, textStyle, textColor) {
        suffixText?.let { text ->
            @Composable {
                Text(
                    text = text,
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Start
                )
            }
        }
    }

    val rememberedLabel = remember(labelText, typography) {
        @Composable {
            if (labelText.isNotEmpty()) {
                Text(
                    text = labelText,
                    style = typography.bodySmall,
                    color = colors.primary,
                    textAlign = TextAlign.Start
                )
            }
        }
    }

    val rememberedLeadingIcon = remember(leadingIcon) {
        leadingIcon?.let { painter ->
            @Composable {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier
                            .size(24.dp)
                            .then(
                                if (isRtl) Modifier.scale(scaleX = -1f, scaleY = 1f)
                                else Modifier
                            )
                    )

                    Box(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .width(1.dp)
                            .height(30.dp)
                            .background(colors.outlineVariant)
                    )
                }
            }
        }
    }

    Column(modifier.thenIfNotNull(onClick) { action ->
        clickableNoRipple {
            focusManager.clearFocus()
            action()
        }
    }) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                val hasEmoji = !allowEmojis && EMOJI_REGEX.containsMatchIn(it)
                if (it.length <= maxChars && !hasEmoji) {
                    onValueChange(it)
                }
            },
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            readOnly = readOnly,
            interactionSource = interaction,
            keyboardOptions = keyboardOptions,
            keyboardActions = effectiveKeyboardActions,
            visualTransformation = visualTransformation,
            isError = showError,
            textStyle = textStyle.copy(
                color = textColor,
                textAlign = TextAlign.Start
            ),
            prefix = rememberedPrefix,
            suffix = rememberedSuffix,
            label = rememberedLabel,
            leadingIcon = rememberedLeadingIcon,
            trailingIcon = if (isLoading || trailingIcon != null) {
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(8.dp))

                        if (showTrailingDivider) {
                            Box(
                                Modifier
                                    .padding(horizontal = 12.dp)
                                    .width(1.dp)
                                    .height(30.dp)
                                    .background(colors.outlineVariant)
                            )
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                color = colors.primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (trailingIcon != null) {
                            Icon(
                                painter = trailingIcon,
                                contentDescription = null,
                                tint = resolvedTrailingIconColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(
                                        scaleX = if (isRtl) -1f else 1f,
                                        scaleY = 1f
                                    )
                                    .thenIf(onTrailingIconClick != null || onClick != null) {
                                        clickableNoRipple {
                                            onTrailingIconClick?.invoke() ?: onClick?.let {
                                                focusManager.clearFocus()
                                                it()
                                            }
                                        }
                                    }
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                }
            } else null,
            shape = shape,
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        val activeFooterText = if (showError) errorText else supportingText
        val activeFooterColor = if (showError) colors.error else colors.onSurfaceVariant

        AnimatedVisibility(
            visible = !activeFooterText.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = activeFooterText ?: "",
                    color = activeFooterColor,
                    modifier = Modifier.padding(start = 16.dp),
                    style = typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
@Preview
fun CustomTextFieldPreview() = Preview {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = "Hello",
            onValueChange = {},
            labelText = "Enter text",
            leadingIcon = painterResource(Res.drawable.ic_close),
            trailingIcon = painterResource(Res.drawable.ic_eye_closed),
//            errorText = "This field is required"
        )
        CustomTextField(
            value = "",
            onValueChange = {},
            labelText = "Enter text",
            leadingIcon = painterResource(Res.drawable.ic_close),
            trailingIcon = painterResource(Res.drawable.ic_eye_closed),
//            errorText = "This field is required"
        )
    }
}
