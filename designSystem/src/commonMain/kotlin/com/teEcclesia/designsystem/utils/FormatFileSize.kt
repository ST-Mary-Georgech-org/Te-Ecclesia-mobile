package com.teEcclesia.designsystem.utils

import kotlin.math.roundToInt

fun formatFileSize(bytes: Long): String {
    if (bytes <= 0L) return ""
    val mb = bytes.toDouble() / (1024.0 * 1024.0)
    return if (mb < 0.1) {
        val kb = bytes.toDouble() / 1024.0
        val roundedKb = (kb * 10).roundToInt() / 10.0
        "$roundedKb KB"
    } else {
        val roundedMb = (mb * 100).roundToInt() / 100.0
        "$roundedMb MB"
    }
}
