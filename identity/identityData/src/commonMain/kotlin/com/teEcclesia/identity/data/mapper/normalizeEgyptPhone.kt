package com.teEcclesia.identity.data.mapper

fun String.normalizeEgyptPhone(): String =
    trim().let { normalizedPhone ->
        if (normalizedPhone.startsWith("+")) normalizedPhone else "+2$normalizedPhone"
    }
