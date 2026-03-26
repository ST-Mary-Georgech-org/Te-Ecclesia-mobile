package com.teEcclesia.designsystem.modifier

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@Composable
fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = MutableInteractionSource(),
    indication: Indication? = null,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = this
    .clickable(
        enabled = enabled,
        interactionSource = remember { interactionSource },
        indication = indication,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )