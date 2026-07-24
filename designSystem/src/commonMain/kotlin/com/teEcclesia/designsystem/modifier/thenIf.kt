package com.teEcclesia.designsystem.modifier

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier =
    if (condition) this.block() else this

inline fun Modifier.thenIfNot(condition: Boolean, block: Modifier.() -> Modifier): Modifier =
    if (!condition) this.block() else this

inline fun <T>Modifier.thenIfNotNull(item: T?, block: Modifier.(T) -> Modifier): Modifier =
    if (item != null) this.block(item) else this