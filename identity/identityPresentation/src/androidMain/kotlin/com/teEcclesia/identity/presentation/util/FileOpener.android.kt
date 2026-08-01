package com.teEcclesia.identity.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.core.content.FileProvider
import java.io.File

class AndroidFileOpener(
    private val context: Context,
    private val uriHandler: UriHandler
) : FileOpener {

    override fun openFile(bytes: ByteArray?, fileName: String?, url: String?) {
        val targetUrl = url ?: fileName?.takeIf { it.startsWith("http://") || it.startsWith("https://") }

        val effectiveName = fileName?.takeIf { it.isNotBlank() && !it.startsWith("http") }
            ?: "document_${System.currentTimeMillis()}"

        if (bytes != null && bytes.isNotEmpty()) {
            try {
                val tempFile = File(context.cacheDir, effectiveName)
                tempFile.writeBytes(bytes)

                val authority = "${context.packageName}.fileprovider"
                val contentUri: Uri = FileProvider.getUriForFile(context, authority, tempFile)

                val extension = effectiveName.substringAfterLast('.', "").lowercase()
                val mimeType = getMimeType(extension)

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(contentUri, mimeType)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                val chooser = Intent.createChooser(intent, "Open with").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
                return
            } catch (e: Exception) {
                if (!targetUrl.isNullOrBlank()) {
                    runCatching { uriHandler.openUri(targetUrl) }
                }
                return
            }
        }

        if (!targetUrl.isNullOrBlank() && (targetUrl.startsWith("http://") || targetUrl.startsWith("https://"))) {
            try {
                val cleanUrl = targetUrl.substringBefore('?')
                val extension = cleanUrl.substringAfterLast('.', "").lowercase()
                val mimeType = getMimeType(extension)

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(targetUrl), mimeType)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                val chooser = Intent.createChooser(intent, "Open with").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
            } catch (e: Exception) {
                runCatching { uriHandler.openUri(targetUrl) }
            }
        }
    }

    private fun getMimeType(extension: String): String {
        return when (extension) {
            "pdf" -> "application/pdf"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
        }
    }
}

@Composable
actual fun rememberFileOpener(): FileOpener {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    return remember(context, uriHandler) {
        AndroidFileOpener(context, uriHandler)
    }
}
