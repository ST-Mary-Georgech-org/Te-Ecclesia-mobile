package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonSize
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.indicator.DotsProgressIndicator
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Deprecated("Don't use it")
fun ScreenTemplate(
    title: String,
    subtitle: String? = null,
    onClickActionButton: () -> Unit,
    actionButtonText: String? = null,
    modifier: Modifier = Modifier,
    actionButtonState: AppButtonState = AppButtonState.Enabled,
    underActionButtonContent: @Composable ColumnScope. () -> Unit = {},
    lowerContent: @Composable ColumnScope. () -> Unit,
) {

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Theme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = Theme.colorScheme.onSurface,
                    style = Theme.typography.headlineMedium
                )
                subtitle?.let {
                    Text(
                        text = it,
                        color = Theme.colorScheme.onSurfaceVariant,
                        style = Theme.typography.bodyMedium
                    )
                }
            }
        }
        item {
            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                lowerContent()

                actionButtonText?.let {
                    AppButton(
                        type = AppButtonType.Primary,
                        size = AppButtonSize.Large,
                        onClick = onClickActionButton,
                        text = actionButtonText,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 40.dp),
                        state = actionButtonState,
                        loadingIcon = {
                            DotsProgressIndicator()
                        }
                    )
                }
                underActionButtonContent()
            }
        }
    }
}

@Preview(heightDp = 800, widthDp = 360)
@Composable
fun ScreenTemplatePreview() = Theme {
    ScreenTemplate(
        title = "Screen Title",
        subtitle = "This is a subtitle",
        onClickActionButton = {},
        actionButtonText = "Action Button",
        underActionButtonContent = {
            Text(
                text = "Under Action Button Content",
                color = Color.Black,
                style = Theme.typography.labelMedium
            )
        }
    ) {
        Column {
            Text("Lower Content Item 1", color = Color.Black, style = Theme.typography.titleLarge)
            Text("Lower Content Item 2", color = Color.Black, style = Theme.typography.titleLarge)
            Text("Lower Content Item 3", color = Color.Black, style = Theme.typography.titleLarge)
        }
    }
}
