package com.teEcclesia.designsystem.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.teEcclesia.designsystem.theme.color.scheme.DarkColorScheme
import com.teEcclesia.designsystem.theme.color.scheme.LightColorScheme
import com.teEcclesia.designsystem.theme.typography.LocalTypography
import androidx.compose.material3.Typography
import com.teEcclesia.designsystem.theme.typography.createThemeTypography
import com.teEcclesia.designsystem.utils.AppLanguage

internal val LocalColorScheme = staticCompositionLocalOf { LightColorScheme }
internal val LocalIsDarkTheme = staticCompositionLocalOf<Boolean?> { null }

@Composable
fun Theme(
    language: String = AppLanguage.English.iso,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = createThemeTypography()

    val layoutDirection = remember(language) {
        if (language.lowercase()
                .startsWith("en") || language == AppLanguage.English.iso
        ) LayoutDirection.Ltr else LayoutDirection.Rtl
    }

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalTypography provides typography,
        LocalIsDarkTheme provides darkTheme,
        LocalLayoutDirection provides layoutDirection
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

object Theme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val typography: Typography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    val isDarkTheme: Boolean
        @Composable @ReadOnlyComposable get() = LocalIsDarkTheme.current ?: isSystemInDarkTheme()
}