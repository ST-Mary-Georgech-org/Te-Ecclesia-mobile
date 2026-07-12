package com.teEcclesia.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.applyIf(
    condition: Boolean,
    newModifiers: @Composable Modifier.() -> Modifier
): Modifier = if (condition) this.newModifiers() else this
