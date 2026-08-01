package com.teEcclesia.identity.presentation.util

import androidx.compose.runtime.Composable

interface FileOpener {
    fun openFile(bytes: ByteArray?, fileName: String?, url: String? = null)
}

@Composable
expect fun rememberFileOpener(): FileOpener
