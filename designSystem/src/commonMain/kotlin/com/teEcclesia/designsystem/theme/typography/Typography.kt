package com.teEcclesia.designsystem.theme.typography

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class Typography(
    val heading: Heading,
    val title: Title,
    val body: Body,
    val label: Label
) {

    data class Heading(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle,
        val extraSmall: TextStyle,
        val tiny: TextStyle
    )

    data class Title(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle
    )

    data class Body(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle,
        val extraSmall: TextStyle,
    )

    data class Label(
        val medium: LabelScale,
        val semiBold: LabelScale
    )

    data class LabelScale(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle,
        val extraSmall: TextStyle,
        val tiny: TextStyle,
        val micro: TextStyle
    )
}

internal val LocalTypography =
    staticCompositionLocalOf<Typography> { error("No Typography provided") }