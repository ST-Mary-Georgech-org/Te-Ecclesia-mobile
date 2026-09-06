package com.teEcclesia.identity.presentation.screen.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.chips.FilterChip
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.presentation.screen.requests.components.RegistrationRequestCard
import com.teEcclesia.identity.presentation.screen.requests.components.SortingOptionBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_menu
import teecclesia.designsystem.generated.resources.ic_search
import teecclesia.designsystem.generated.resources.registration_requests
import teecclesia.designsystem.generated.resources.search_requests

@Composable
fun RegistrationRequestsScreen(
    viewModel: RegistrationRequestsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        RegistrationRequestsContent(
            state = state,
            listener = viewModel
        )
    }
}

@Composable
private fun RegistrationRequestsContent(
    state: RegistrationRequestsUiState,
    listener: RegistrationRequestsInteractionListener
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
    ) {
        LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .statusBarsPadding(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "header_title") {
                    Text(
                        text = stringResource(Res.string.registration_requests),
                        style = Theme.typography.titleLarge,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                stickyHeader(key = "search_header") {
                    CustomTextField(
                        value = state.searchQuery,
                        shape = RoundedCornerShape(28.dp),
                        onValueChange = listener::onSearchQueryChanged,
                        labelText = stringResource(Res.string.search_requests),
                        trailingIcon = painterResource(Res.drawable.ic_menu),
                        leadingIcon = painterResource(Res.drawable.ic_search),
                        onTrailingIconClick = { listener.onToggleSortingSheet(true) },
                        backgroundColor = Theme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                item(key = "roles_filter_header") {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            items = state.roles,
                            key = { role -> role?.name ?: "ALL" }
                        ) { role ->
                            FilterChip(
                                selected = state.selectedRole == role,
                                onClick = { listener.onRoleFilterSelected(role) },
                                label = {
                                    Text(
                                        text = stringResource(role.toText()),
                                        style = Theme.typography.bodyMedium,
                                        color = if (state.selectedRole == role) {
                                            Theme.colorScheme.onSecondaryContainer
                                        } else {
                                            Theme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (state.isLoading || state.isRefreshing) {
                    item(key = "loading_state") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Theme.colorScheme.primary)
                        }
                    }
                } else {
                    items(state.requests, key = { it.id }) { request ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp).animateItem()) {
                            RegistrationRequestCard(
                                onClick = { listener.onRequestClicked(request.id) },
                                imageUrl = request.imageUrl,
                                fullName = request.fullName,
                                requestDateTime = request.createdAt,
                                role = request.role,
                                stage = when (request.role) {
                                    UserRole.KHADEM -> request.khademProfile?.educationalStage
                                    UserRole.MAKHDOOM -> request.makhdoomProfile?.educationalStage
                                    else -> null
                                },
                                year = when (request.role) {
                                    UserRole.KHADEM -> request.khademProfile?.educationalYear
                                    UserRole.MAKHDOOM -> request.makhdoomProfile?.educationalYear
                                    else -> null
                                },
                                shamamsaStudyStatus = if (request.role == UserRole.MAKHDOOM) request.makhdoomProfile?.shamamsaStudyStatus else null,
                            )
                        }
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
            }

        PaginationTrigger(
            list = state.requests,
            listState = listState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadMore
        )
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
