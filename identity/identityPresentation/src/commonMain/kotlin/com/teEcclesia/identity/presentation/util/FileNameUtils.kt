package com.teEcclesia.identity.presentation.util

import com.teEcclesia.designsystem.utils.formatFileSize
import com.teEcclesia.shared.domain.model.SafeByteArray

fun getDisplayFileName(
    fileTitle: String?,
    fileName: String?,
    fileBytes: SafeByteArray? = null,
    fileSizeBytes: Long? = null
): String {
    if (fileTitle.isNullOrBlank() && fileName.isNullOrBlank()) return ""
    val rawName = if (fileTitle.isNullOrBlank() || fileName.isNullOrBlank()) {
        fileName?.substringBefore('?')?.substringAfterLast('/').orEmpty()
    } else {
        val ext = fileName.substringBefore('?').substringAfterLast('.', "").lowercase()
        val validExt = if (ext in listOf("pdf", "jpg", "jpeg", "png", "webp")) ext else "jpg"
        "$fileTitle.$validExt"
    }

    val size = fileSizeBytes ?: fileBytes?.size?.toLong()
    val formattedSize = if (size != null && size > 0L) formatFileSize(size) else ""

    return if (formattedSize.isNotEmpty() && rawName.isNotEmpty()) {
        "$rawName ($formattedSize)"
    } else {
        rawName
    }
}

fun detectFileExtension(bytes: ByteArray): String {
    if (bytes.size >= 4) {
        if (bytes[0] == 0x25.toByte() && bytes[1] == 0x50.toByte() && bytes[2] == 0x44.toByte() && bytes[3] == 0x46.toByte()) {
            return "pdf"
        }
        if (bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() && bytes[2] == 0x4E.toByte() && bytes[3] == 0x47.toByte()) {
            return "png"
        }
    }
    if (bytes.size >= 3 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte() && bytes[2] == 0xFF.toByte()) {
        return "jpg"
    }
    if (bytes.size >= 12 && bytes[0] == 0x52.toByte() && bytes[1] == 0x49.toByte() && bytes[2] == 0x46.toByte() && bytes[3] == 0x46.toByte()
        && bytes[8] == 0x57.toByte() && bytes[9] == 0x45.toByte() && bytes[10] == 0x42.toByte() && bytes[11] == 0x50.toByte()) {
        return "webp"
    }
    return "jpg"
}

@OptIn(kotlin.time.ExperimentalTime::class)
fun generateScannedFileName(bytes: ByteArray): String {
    val ext = detectFileExtension(bytes)
    val timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()
    return "scan_$timestamp.$ext"
}

