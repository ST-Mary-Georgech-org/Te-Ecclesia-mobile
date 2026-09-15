package com.teEcclesia.shared.domain.utils.validation

fun validateFileSizes(file: ByteArray): Boolean {
    val maxFileSize = 15 * 1024 * 1024 // 15 MB
    return file.size <= maxFileSize
}
