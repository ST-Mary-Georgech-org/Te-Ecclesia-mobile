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
