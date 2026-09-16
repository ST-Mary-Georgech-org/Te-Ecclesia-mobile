package com.teEcclesia.notifications.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.cards.NotificationCard
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.format
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.model.NotificationType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.failed_to_load_notifications
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_bell
import teecclesia.designsystem.generated.resources.ic_error
import teecclesia.designsystem.generated.resources.no_notifications
import teecclesia.designsystem.generated.resources.notifications
import teecclesia.designsystem.generated.resources.retry

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NotificationsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
fun NotificationsContent(
    state: NotificationsUiState,
    listener: NotificationsInteractionListener
) {
    val listState = rememberLazyListState()

    PaginationTrigger(
        list = state.notifications,
        listState = listState,
        remainingItemsToLoadNextPage = 3,
        loadNextItems = listener::onLoadMoreNotifications
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Theme.colorScheme.onBackground,
                modifier = Modifier.clickableNoRipple { listener.onClickBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(Res.string.notifications),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = listener::onReload
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Theme.colorScheme.primary)
                    }
                }
                state.isError && state.notifications.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_error),
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = Theme.colorScheme.error
                            )
                            Text(
                                text = stringResource(Res.string.failed_to_load_notifications),
                                style = Theme.typography.titleMedium,
                                color = Theme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                            AppButton(
                                type = AppButtonType.Primary,
                                text = stringResource(Res.string.retry),
                                onClick = listener::onReload
                            )
                        }
                    }
                }
                state.notifications.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Theme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_bell),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = Theme.colorScheme.primary
                                )
                            }
                            Text(
                                text = stringResource(Res.string.no_notifications),
                                style = Theme.typography.titleMedium,
                                color = Theme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.notifications,
                            key = { it.id }
                        ) { notification ->
                            val timeString = notification.sentAt.time.format()
                            val dateString = "${notification.sentAt.date.month.name.take(3)} ${notification.sentAt.date.day} • $timeString"

                            NotificationCard(
                                icon = Res.drawable.ic_bell,
                                title = notification.title,
                                description = notification.message,
                                current = dateString,
                                backgroundColor = if (notification.isRead) Theme.colorScheme.surface else Theme.colorScheme.surfaceVariant
                            )
                        }

                        if (state.isLoadingMore) {
                            item(key = "loading_more") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Theme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationsScreenPreview() {
    Theme {
        NotificationsContent(
            state = NotificationsUiState(
                notifications = listOf(
                    NotificationResponse(
                        id = "1",
                        title = "تنبيه جديد",
                        message = "تم تفعيل الحساب بنجاح",
                        type = NotificationType.ALERT,
                        sentAt = LocalDate(2026, 1, 1).atTime(0, 0),
                        isRead = false
                    )
                )
            ),
            listener = object : NotificationsInteractionListener {
                override fun onClickBack() {}
                override fun onLoadMoreNotifications() {}
                override fun onReload() {}
            }
        )
    }
}
