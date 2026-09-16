package com.teEcclesia.identity.data.utils

fun calculateUploadTimeoutMillis(vararg files: ByteArray?): Long {
    val totalBytes = files.filterNotNull().sumOf { it.size.toLong() }
    val baseTimeoutMillis = 60_000L
    val totalMB = (totalBytes + (1024 * 1024 - 1)) / (1024 * 1024)
    val additionalTimeoutMillis = totalMB * 30_000L
    return baseTimeoutMillis + additionalTimeoutMillis
}
