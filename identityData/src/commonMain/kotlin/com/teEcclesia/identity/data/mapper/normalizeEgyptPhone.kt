package com.teEcclesia.identity.data.mapper

fun String.normalizeEgyptPhone(): String =
    trim().let { normalizedPhone ->
        if (normalizedPhone.startsWith("+2")) normalizedPhone else "+2$normalizedPhone"
    }