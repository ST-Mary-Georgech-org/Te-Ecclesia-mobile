package com.teEcclesia.designsystem.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teEcclesia.designsystem.theme.theme.Theme
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.home

enum class AppButtonState {
    Enabled,
    Disabled,
    Loading
}

enum class AppButtonSize {
    Small,
    Large
}

enum class IconPosition {
    Start, End
}


sealed class AppButtonType {
    object Primary : AppButtonType()
    object Secondary : AppButtonType()
    object Tertiary : AppButtonType()
}

@Composable
fun AppButton(
    type: AppButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: AppButtonSize = AppButtonSize.Large,
    state: AppButtonState = AppButtonState.Enabled,
    text: String? = null,
    disableTertiaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    disablePrimaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    disableSecondaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    enableTertiaryBackgroundColor: Color = Theme.colorScheme.tertiary,
    enablePrimaryBackgroundColor: Color = Theme.colorScheme.primary,
    enableSecondaryBackgroundColor: Color = Theme.colorScheme.secondary,
    loadingIcon: @Composable () -> Unit = {
        CircularProgressIndicator(
            color = Theme.colorScheme.onPrimary,
            strokeWidth = 2.dp,
            modifier = Modifier.size(20.dp)
        )
    },
    icon: ImageVector? = null,
    iconPosition: IconPosition? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isLoading = (state == AppButtonState.Loading)
    val isLarge = (size == AppButtonSize.Large)
    val enabled = (state == AppButtonState.Enabled)

    val backgroundColor = getBackgroundColor(
        type = type,
        isDisabled = (state == AppButtonState.Disabled),
        disableTertiaryBackgroundColor = disableTertiaryBackgroundColor,
        disablePrimaryBackgroundColor = disablePrimaryBackgroundColor,
        disableSecondaryBackgroundColor = disableSecondaryBackgroundColor,
        enableTertiaryBackgroundColor = enableTertiaryBackgroundColor,
        enablePrimaryBackgroundColor = enablePrimaryBackgroundColor,
        enableSecondaryBackgroundColor = enableSecondaryBackgroundColor
    )
    val buttonContentColor = getContentColor(state, type)

    Surface(
        modifier = modifier.height(IntrinsicSize.Min),
        shape = RoundedCornerShape(16.dp),
        contentColor = buttonContentColor,
        onClick = { if (enabled) onClick() },
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(getAppContentPadding(type, isLoading, isLarge)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null && iconPosition == IconPosition.Start && !isLoading) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = buttonContentColor,

                    )
                if (!text.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            text?.let {
                Text(
                    text = it,
                    style = if (isLarge)
                        Theme.typography.titleMedium
                    else
                        Theme.typography.bodyMedium,
                    color = buttonContentColor
                )
            }

            if (icon != null && iconPosition == IconPosition.End && !isLoading) {
                if (!text.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = buttonContentColor
                )
            }

            if(state == AppButtonState.Loading) {
                AnimatedVisibility(
                    visible = state == AppButtonState.Loading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(8.dp))
                        loadingIcon()
                    }
                }
            }
        }
    }
}


@Composable
private fun getBackgroundColor(
    type: AppButtonType,
    isDisabled: Boolean,
    disableTertiaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    disablePrimaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    disableSecondaryBackgroundColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.1f),
    enableTertiaryBackgroundColor: Color = Theme.colorScheme.tertiary,
    enablePrimaryBackgroundColor: Color = Theme.colorScheme.primary,
    enableSecondaryBackgroundColor: Color = Theme.colorScheme.secondary,
): Color {
    return if (isDisabled) {
        when (type) {
            AppButtonType.Tertiary -> disableTertiaryBackgroundColor
            AppButtonType.Primary -> disablePrimaryBackgroundColor
            AppButtonType.Secondary -> disableSecondaryBackgroundColor
        }
    } else {
        when (type) {
            AppButtonType.Primary -> enablePrimaryBackgroundColor
            AppButtonType.Secondary -> enableSecondaryBackgroundColor
            AppButtonType.Tertiary -> enableTertiaryBackgroundColor
        }
    }
}


@Composable
private fun getContentColor(
    state: AppButtonState,
    type: AppButtonType,
): Color {
    return when {
        state == AppButtonState.Disabled -> Theme.colorScheme.onSurface.copy(alpha = 0.1f)
        else -> when (type) {
            AppButtonType.Primary -> Theme.colorScheme.onPrimary
            AppButtonType.Secondary -> Theme.colorScheme.onSecondary
            AppButtonType.Tertiary -> Theme.colorScheme.onTertiary
        }
    }
}

@Composable
private fun getAppContentPadding(
    type: AppButtonType,
    isLoading: Boolean,
    isLarge: Boolean,
): PaddingValues {
    val horizontalPadding = animateDpAsState(if (isLarge) 24.dp else 16.dp)
    if (isLoading) {
        return when (type) {
            AppButtonType.Primary, AppButtonType.Secondary -> PaddingValues(
                horizontal = horizontalPadding.value,
                vertical = animateDpAsState(if (isLarge) 12.dp else 8.dp).value
            )

            AppButtonType.Tertiary -> PaddingValues(
                horizontal = horizontalPadding.value,
                vertical = 4.dp
            )


        }
    } else {
        return when (type) {
            AppButtonType.Primary, AppButtonType.Secondary -> PaddingValues(
                horizontal = horizontalPadding.value,
                vertical = animateDpAsState(if (isLarge) 15.dp else 10.dp).value
            )

            AppButtonType.Tertiary -> PaddingValues(
                horizontal = horizontalPadding.value,
                vertical = animateDpAsState(if (isLarge) 7.dp else 9.dp).value
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppPrimaryLargeButtonLoadingPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Primary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large,
            state = AppButtonState.Loading
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppPrimaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Primary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppSecondaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Secondary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppTertiaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Tertiary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppDisablePrimaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Primary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large,
            state = AppButtonState.Disabled
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppDisableSecondaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Secondary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large,
            state = AppButtonState.Disabled
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppDisableTertiaryLargeButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Tertiary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Large,
            state = AppButtonState.Disabled
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppPrimarySmallButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Primary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Small,
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppSecondarySmallButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Secondary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Small,
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AppTertiarySmallButtonPreview() {
    Theme {
        AppButton(
            type = AppButtonType.Tertiary,
            onClick = {},
            text = stringResource(Res.string.home),
            size = AppButtonSize.Small,
        )
    }
}

