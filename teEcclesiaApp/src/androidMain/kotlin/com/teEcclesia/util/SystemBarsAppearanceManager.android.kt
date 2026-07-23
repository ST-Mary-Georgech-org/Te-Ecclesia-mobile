package com.teEcclesia.util

import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.util.AppTheme

@Composable
actual fun SetSystemBarsAppearance(appTheme: AppTheme?) {
    val context = LocalContext.current
    val navColor = Theme.colorScheme.background
    val isSystemInDarkTheme = isSystemInDarkTheme()

    SideEffect {
        val activity = context as? ComponentActivity ?: return@SideEffect
        val window = activity.window
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        val theme = when (appTheme) {
            AppTheme.LIGHT -> true
            AppTheme.DARK -> false
            AppTheme.SYSTEM, null -> !isSystemInDarkTheme
        }
        controller.isAppearanceLightStatusBars = theme
        controller.isAppearanceLightNavigationBars = theme
        window.navigationBarColor = navColor.toArgb()
    }
}
