package com.teEcclesia.identity.presentation.screen.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.SearchBar
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.presentation.screen.requests.components.RegistrationRequestCard
import com.teEcclesia.identity.presentation.screen.requests.components.SortingOptionBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.requests
import teecclesia.designsystem.generated.resources.search_requests

@Composable
fun RegistrationRequestsScreen(
    viewModel: RegistrationRequestsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    RegistrationRequestsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun RegistrationRequestsContent(
    state: RegistrationRequestsUiState,
    listener: RegistrationRequestsInteractionListener
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(top = 16.dp)
    ) {
        Text(
            text = Res.string.requests.asString(),
            style = Theme.typography.titleLarge,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = state.searchQuery,
            onQueryChange = listener::onSearchQueryChanged,
            placeholder = Res.string.search_requests.asString(),
            trailingIcon = painterResource(Res.drawable.ic_chevron_down),
            onTrailingIconClick = { listener.onToggleSortingSheet(true) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.roles) { role ->
                val title = role.toText().asString()
                val isSelected = state.selectedRole == role

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) Theme.colorScheme.primary else Theme.colorScheme.surface
                        )
                        .clickable { listener.onRoleFilterSelected(role) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = title,
                        style = Theme.typography.bodyMedium,
                        color = if (isSelected) Theme.colorScheme.onPrimary else Theme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = listener::onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading && !state.isRefreshing) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Theme.colorScheme.primary)
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.requests, key = { it.id }) { request ->
                            RegistrationRequestCard(
                                profile = request,
                                onClick = {
                                    // Detail view click
                                }
                            )
                        }

                        if (state.isPagingLoading) {
                            item {
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
                }

                PaginationTrigger(
                    list = state.requests,
                    listState = listState,
                    remainingItemsToLoadNextPage = 5,
                    loadNextItems = listener::onLoadMore
                )
            }
        }

        SortingOptionBottomSheet(
            isVisible = state.isSortingSheetVisible,
            currentSortBy = state.sortBy,
            currentSortOrder = state.sortOrder,
            onSortOptionSelected = { sortBy, sortOrder ->
                listener.onSortByChanged(sortBy, sortOrder)
                listener.onToggleSortingSheet(false)
            },
            onDismiss = { listener.onToggleSortingSheet(false) }
        )
    }
}
