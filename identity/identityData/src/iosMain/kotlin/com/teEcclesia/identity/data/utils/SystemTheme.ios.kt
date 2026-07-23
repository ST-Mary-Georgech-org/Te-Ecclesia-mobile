package com.teEcclesia.identity.data.utils

import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

actual val isSystemDarkTheme: Boolean
    get() {
        val style = UIScreen.mainScreen.traitCollection.userInterfaceStyle
        return style == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    }
