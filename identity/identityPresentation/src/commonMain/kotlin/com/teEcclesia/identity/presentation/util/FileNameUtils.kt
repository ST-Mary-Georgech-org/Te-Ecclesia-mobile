package com.teEcclesia.identity.presentation.util

fun getDisplayFileName(fileTitle: String?, fileName: String?): String {
    if (fileTitle.isNullOrBlank() || fileName.isNullOrBlank()) {
        return fileName?.substringBefore('?')?.substringAfterLast('/').orEmpty()
    }
    val ext = fileName.substringBefore('?').substringAfterLast('.', "").lowercase()
    val validExt = if (ext in listOf("pdf", "jpg", "jpeg", "png", "webp")) ext else "jpg"
    return "$fileTitle.$validExt"
}
