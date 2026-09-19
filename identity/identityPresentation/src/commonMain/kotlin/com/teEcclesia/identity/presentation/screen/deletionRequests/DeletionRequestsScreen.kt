package com.teEcclesia.identity.presentation.screen.deletionRequests

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.AccountDeletionRequest
import com.teEcclesia.identity.presentation.screen.deletionRequests.components.DeletionRequestCard
import com.teEcclesia.identity.presentation.screen.deletionRequests.components.DeletionRequestCardShimmer
import com.teEcclesia.shared.domain.model.UserRole
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.deletion_requests
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.no_deletion_requests_found

@Composable
fun DeletionRequestsScreen(
    viewModel: DeletionRequestsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        DeletionRequestsContent(
            state = state,
            listener = viewModel
        )
    }
}

@Composable
private fun DeletionRequestsContent(
    state: DeletionRequestsUiState,
    listener: DeletionRequestsInteractionListener
) {
    val listState = rememberLazyListState()

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
                text = stringResource(Res.string.deletion_requests),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        if (state.isLoading && !state.isRefreshing) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(count = 6) {
                    DeletionRequestCardShimmer()
                }
            }
        } else if (state.requests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_deletion_requests_found),
                    style = Theme.typography.bodyLarge,
                    color = Theme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.requests,
                    key = { it.id }
                ) { request ->
                    DeletionRequestCard(
                        request = request,
                        onClick = { listener.onRequestClicked(request) }
                    )
                }

                if (state.isPagingLoading) {
                    item(key = "paging_loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Theme.colorScheme.primary)
                        }
                    }
                }
            }

            PaginationTrigger(
                list = state.requests,
                listState = listState,
                remainingItemsToLoadNextPage = 5,
                loadNextItems = listener::onLoadMore
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeletionRequestsContentPreview() = Theme {
    DeletionRequestsContent(
        state = DeletionRequestsUiState(
            isLoading = false,
            requests = listOf(
                AccountDeletionRequest(
                    id = "1",
                    userId = "usr-1",
                    userName = "مينا يسي كامل",
                    userCode = "M12345678",
                    userImageUrl = null,
                    userRole = UserRole.MAKHDOOM,
                    reason = "ظروف خاصة تمنعني من المتابعة",
                    requestedAt = LocalDateTime(2026, 9, 16, 12, 0)
                ),
                AccountDeletionRequest(
                    id = "2",
                    userId = "usr-2",
                    userName = "جورج سمير",
                    userCode = "K87654321",
                    userImageUrl = null,
                    userRole = UserRole.KHADEM,
                    reason = "الانتقال إلى كنيسة أخرى",
                    requestedAt = LocalDateTime(2026, 9, 15, 10, 30)
                )
            )
        ),
        listener = object : DeletionRequestsInteractionListener {
            override fun onRefresh() {}
            override fun onLoadMore() {}
            override fun onRequestClicked(request: AccountDeletionRequest) {}
            override fun onClickBack() {}
        }
    )
}
