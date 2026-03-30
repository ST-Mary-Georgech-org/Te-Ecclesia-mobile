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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.TeEcclesiaPreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_home

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    trailingIconColor: Color? = null,
    backgroundColor: Color = Color.Unspecified,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    onTrailingIconClick: () -> Unit = {},
    showTrailingDivider: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    val colors = Theme.colorScheme
    val typography = Theme.typography

    val interaction = remember { MutableInteractionSource() }
    val showError = !errorText.isNullOrBlank()

    val currentDirection = LocalLayoutDirection.current
    val isRtl = currentDirection == LayoutDirection.Rtl

    val resolvedTrailingIconColor = trailingIconColor ?: animateColorAsState(
        targetValue = if (showError) colors.additional.onError else colors.text.label,
        animationSpec = tween(durationMillis = 150)
    ).value

    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            readOnly = readOnly,
            interactionSource = interaction,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            isError = showError,
            textStyle = typography.body.small.copy(
                color = colors.text.title,
                textAlign = TextAlign.Start
            ),
            prefix = prefix,
            suffix = suffix,
            label = {
                if (hint.isNotEmpty()) {
                    Text(
                        text = hint,
                        style = typography.body.small,
                        textAlign = TextAlign.Start
                    )
                }
            },
            leadingIcon = leadingIcon?.let { painter ->
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = colors.text.label,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(
                                    scaleX = if (isRtl) -1f else 1f,
                                    scaleY = 1f
                                )
                        )

                        Box(
                            Modifier
                                .padding(horizontal = 12.dp)
                                .width(1.dp)
                                .height(30.dp)
                                .background(colors.border.secondary)
                        )
                    }
                }
            },
            trailingIcon = trailingIcon?.let { painter ->
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
                                    .background(colors.border.secondary)
                            )
                        }

                        Icon(
                            painter = painter,
                            contentDescription = null,
                            tint = resolvedTrailingIconColor,
                            modifier = Modifier
                                .size(24.dp)
                                .scale(
                                    scaleX = if (isRtl) -1f else 1f,
                                    scaleY = 1f
                                )
                                .clickableNoRipple(onClick = onTrailingIconClick)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            },
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.text.title,
                unfocusedTextColor = colors.text.title,
                disabledTextColor = colors.text.label,
                errorTextColor = colors.text.title,
                focusedBorderColor = colors.border.active,
                unfocusedBorderColor = colors.border.secondary,
                disabledBorderColor = colors.border.secondary,
                errorBorderColor = colors.additional.onError,
                focusedLabelColor = colors.border.active,
                unfocusedLabelColor = colors.text.label,
                disabledLabelColor = colors.text.label,
                errorLabelColor = colors.additional.onError,
                cursorColor = colors.border.active,
                errorCursorColor = colors.additional.onError,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                errorContainerColor = backgroundColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
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
                modifier = Modifier.padding(start = 16.dp),
                style = Theme.typography.body.small,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
@Preview
fun CustomTextFieldPreview() = TeEcclesiaPreview {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CustomTextField(
            value = "Hello",
            onValueChange = {},
            hint = "Enter text",
            leadingIcon = painterResource(Res.drawable.ic_home),
            trailingIcon = painterResource(Res.drawable.ic_eye_closed),
            errorText = "This field is required"
        )
        CustomTextField(
            value = "",
            onValueChange = {},
            hint = "Enter text",
            leadingIcon = painterResource(Res.drawable.ic_home),
            trailingIcon = painterResource(Res.drawable.ic_eye_closed),
            errorText = "This field is required"
        )
    }
}