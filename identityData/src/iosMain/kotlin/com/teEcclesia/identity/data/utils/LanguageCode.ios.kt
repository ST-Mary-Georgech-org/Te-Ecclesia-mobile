package com.teEcclesia.identity.data.utils

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual val languageCode: String
    get() {
        val firstLanguage = NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"
        return firstLanguage.substringBefore("-")
    }
