package com.teEcclesia.designsystem.components.pdf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.theme.theme.Theme

@Composable
fun PdfViewer(
    pdf: ByteArray,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    onPagesReady: (Boolean) -> Unit = {}
) {
    var pages by remember(pdf) { mutableStateOf<List<PdfPage>?>(null) }

    LaunchedEffect(pdf) {
        pages = splitPdfToPngs(pdfData = pdf)
        onPagesReady(true)
    }

    if (pages == null) {
        onPagesReady(false)
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Theme.colorScheme.primary
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            userScrollEnabled = userScrollEnabled,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(pages.orEmpty()) { page ->
                AsyncImage(
                    model = page.pngData,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(page.aspectRatio)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                )
            }
        }
    }
}
