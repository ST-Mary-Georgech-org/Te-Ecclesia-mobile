package com.teEcclesia.designsystem.utils

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.designsystem.theme.theme.Theme

@Composable
fun TeEcclesiaPreview(
    modifier: Modifier = Modifier,
    color: Color = Theme.colorScheme.background.tertiary,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    TeEcclesiaTheme(darkTheme = darkTheme) {
        Surface(modifier = modifier, color = color) {
            content()
        }
    }
}