package com.teEcclesia.designsystem.theme.color.scheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorScheme(
    val text: Text,
    val additional: Additional,
    val background: Background,
    val border: Border,
    val brand: Brand,
    val button: Button,
    val icon: Icon,
    val primary: PrimaryVariant,
) {
    data class Text(
        val body: Color,
        val bodyBlue: Color,
        val headingBlue: Color,
        val label: Color,
        val link: Color,
        val title: Color,
        val titleSmall: Color
    )

    data class Additional(
        val blue: Color,
        val error: Color,
        val onError: Color,
        val onSuccess: Color,
        val onDone: Color,
        val onWarning: Color,
        val pink: Color,
        val purple: Color,
        val success: Color,
        val warning: Color,
        val done: Color
    )

    data class Background(
        val octonary: Color,
        val quaternary: Color,
        val quinary: Color,
        val senary: Color,
        val septenary: Color,
        val primary: Color,
        val secondary: Color,
        val tertiary: Color
    )

    data class Border(
        val active: Color,
        val primary: Color,
        val secondary: Color,
        val tertiary: Color
    )

    data class Brand(
        val onPrimary: Color,
        val onSecondary: Color,
        val primary: Color,
        val primaryGradient1: Color,
        val primaryGradient2: Color,
        val primaryVariant: Color,
        val secondary: Color,
        val secondaryVariant: Color
    )

    data class Button(
        val onPrimary: Color,
        val onQuaternary: Color,
        val onQuinary: Color,
        val onSecondary: Color,
        val onTertiary: Color,
        val primary: Color,
        val quaternary: Color,
        val secondary: Color,
        val quinary: Color,
        val tertiary: Color
    )

    data class Icon(
        val primary: Color,
        val secondary: Color,
        val tertiary: Color
    )

    data class PrimaryVariant(
        val variant50: Color,
        val variant100: Color,
        val variant200: Color,
        val variant300: Color,
        val variant400: Color,
        val variant500: Color,
        val variant600: Color,
        val variant700: Color,
        val variant800: Color,
        val variant900: Color,
        val variant950: Color
    )
}

internal val LocalColorScheme = staticCompositionLocalOf { LightColorScheme }
