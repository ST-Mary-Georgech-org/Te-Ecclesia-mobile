package com.teEcclesia.identity.data.utils

import android.content.res.Configuration
import android.content.res.Resources

actual val isSystemDarkTheme: Boolean
    get() {
        val uiMode = Resources.getSystem().configuration.uiMode
        return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
