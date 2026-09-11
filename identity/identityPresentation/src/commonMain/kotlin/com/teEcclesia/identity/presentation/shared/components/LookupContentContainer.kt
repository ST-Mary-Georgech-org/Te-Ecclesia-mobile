package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.shimmerEffect
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.retry

@Composable
fun LookupContentContainer(
    isLoading: Boolean,
    isError: Boolean,
    isEmpty: Boolean,
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    shimmerCount: Int = 4,
    content: @Composable () -> Unit
) {
    when {
        isError -> {
            LookupErrorContent(
                message = errorMessage,
                onRetry = onRetry,
                modifier = modifier
            )
        }
        isLoading && isEmpty -> {
            LookupShimmerContent(
                count = shimmerCount,
                modifier = modifier
            )
        }
        else -> {
            content()
        }
    }
}

@Composable
fun LookupShimmerContent(
    count: Int = 4,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(count) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun LookupErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = Theme.typography.bodyLarge,
            color = Theme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            type = AppButtonType.Primary,
            text = stringResource(Res.string.retry),
            onClick = onRetry
        )
    }
}

@Preview
@Composable
private fun LookupContentContainerShimmerPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            LookupContentContainer(
                isLoading = true,
                isError = false,
                isEmpty = true,
                errorMessage = "Error",
                onRetry = {},
                content = {}
            )
        }
    }
}

@Preview
@Composable
private fun LookupContentContainerErrorPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            LookupContentContainer(
                isLoading = false,
                isError = true,
                isEmpty = true,
                errorMessage = "حدث خطأ أثناء تحميل البيانات",
                onRetry = {},
                content = {}
            )
        }
    }
}
