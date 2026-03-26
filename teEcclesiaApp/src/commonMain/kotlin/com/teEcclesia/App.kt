package com.teEcclesia

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.teEcclesia.designsystem.theme.theme.TeEcclesiaTheme
import com.teEcclesia.appEntryPoint.EntryPoint
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun App(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme()
) {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(500)
        showSplash = false
    }

    TeEcclesiaTheme(
        darkTheme = isSystemDarkTheme,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (showSplash) {
                    SharedSplashScreen()
                } else {
                    EntryPoint()
                }
            }
        }
    )
}
