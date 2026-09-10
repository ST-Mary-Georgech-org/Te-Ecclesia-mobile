package com.teEcclesia.designsystem.components.pdf

expect suspend fun splitPdfToPngs(pdfData: ByteArray): List<PdfPage>
