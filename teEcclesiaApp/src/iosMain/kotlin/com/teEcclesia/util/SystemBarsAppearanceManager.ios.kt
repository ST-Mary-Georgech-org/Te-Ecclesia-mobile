package com.teEcclesia.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.teEcclesia.identity.domain.util.AppTheme
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun SetSystemBarsAppearance(appTheme: AppTheme?) {
    val isSystemInDarkTheme = isSystemInDarkTheme()

    SideEffect {
        dispatch_async(dispatch_get_main_queue()) {
            val theme = when (appTheme) {
                AppTheme.LIGHT -> true
                AppTheme.DARK -> false
                AppTheme.SYSTEM, null -> !isSystemInDarkTheme
            }
            val style = if (theme) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
            UIApplication.sharedApplication.setStatusBarStyle(style, animated = true)
        }
    }
}
