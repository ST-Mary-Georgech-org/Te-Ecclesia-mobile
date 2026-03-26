package com.teEcclesia

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.appEntryPoint.EntryPoint
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun App(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme()
) {
    TeEcclesiaTheme(
        darkTheme = isSystemDarkTheme,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                EntryPoint()
            }
        }
    )
}
