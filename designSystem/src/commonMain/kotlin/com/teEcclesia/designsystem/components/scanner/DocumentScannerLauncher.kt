package com.teEcclesia.designsystem.components.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import io.github.kalinjul.easydocumentscan.DocumentCaptureMode
import io.github.kalinjul.easydocumentscan.DocumentScannerModeAndroid
import io.github.kalinjul.easydocumentscan.DocumentScannerOptions
import io.github.kalinjul.easydocumentscan.DocumentScannerOptionsAndroid
import io.github.kalinjul.easydocumentscan.DocumentScannerOptionsIos
import io.github.kalinjul.easydocumentscan.rememberDocumentScanner
import kotlinx.coroutines.launch

@Composable
fun DocumentScannerLauncher(
    shouldOpen: Boolean,
    onScannerOpened: () -> Unit,
    onResult: (ByteArray?) -> Unit,
    pageLimit: Int = 1,
    allowGalleryImport: Boolean = false,
    scannerMode: DocumentScannerModeAndroid = DocumentScannerModeAndroid.BASE,
    captureMode: DocumentCaptureMode = DocumentCaptureMode.MANUAL
) {
    val scope = rememberCoroutineScope()

    val scanner = rememberDocumentScanner(
        onResult = { result ->
            scope.launch {
                result
                    .onSuccess { images ->
                        val image = images.firstOrNull()
                        val bytes = image?.loadBytes()

                        onResult(bytes)
                    }
                    .onFailure { error ->
                        println("Document scanner error: $error")
                        onResult(null)
                    }
            }
        },
        options = DocumentScannerOptions(
            DocumentScannerOptionsAndroid(
                pageLimit = pageLimit,
                allowGalleryImport = allowGalleryImport,
                scannerMode = scannerMode
            ),
            DocumentScannerOptionsIos(
                captureMode = captureMode
            )
        )
    )

    LaunchedEffect(shouldOpen) {
        if (shouldOpen) {
            onScannerOpened()
            scanner.scan()
        }
    }
}