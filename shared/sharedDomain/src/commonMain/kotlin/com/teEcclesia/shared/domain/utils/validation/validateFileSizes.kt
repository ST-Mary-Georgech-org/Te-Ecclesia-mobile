package com.teEcclesia.shared.domain.utils.validation

fun validateFileSizes(file: ByteArray): Boolean {
    val maxFileSize = 10 * 1024 * 1024 // 10 MB
    return file.size <= maxFileSize
}
