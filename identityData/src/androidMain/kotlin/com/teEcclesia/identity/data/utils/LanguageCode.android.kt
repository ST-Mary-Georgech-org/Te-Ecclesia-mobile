package com.teEcclesia.identity.data.utils

import java.util.Locale

actual val languageCode: String
    get() = Locale.getDefault().language