package com.teEcclesia.identity.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler

class IosFileOpener(
    private val uriHandler: UriHandler
) : FileOpener {

    override fun openFile(bytes: ByteArray?, fileName: String?, url: String?) {
        val targetUrl = url ?: fileName?.takeIf { it.startsWith("http://") || it.startsWith("https://") }
        if (!targetUrl.isNullOrBlank()) {
            runCatching { uriHandler.openUri(targetUrl) }
        }
    }
}

@Composable
actual fun rememberFileOpener(): FileOpener {
    val uriHandler = LocalUriHandler.current
    return remember(uriHandler) {
        IosFileOpener(uriHandler)
    }
}
