package com.teEcclesia.identity.presentation.util

import androidx.compose.runtime.Composable
import com.teEcclesia.shared.domain.model.SafeByteArray

interface FileOpener {
    fun openFile(bytes: ByteArray?, fileName: String?, url: String? = null)
    fun openFile(bytes: SafeByteArray?, fileName: String?, url: String? = null) {
        openFile(bytes?.bytes, fileName, url)
    }
}

@Composable
expect fun rememberFileOpener(): FileOpener
