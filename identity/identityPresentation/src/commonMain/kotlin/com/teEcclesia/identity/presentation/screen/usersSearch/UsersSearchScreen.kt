package com.teEcclesia.identity.presentation.screen.usersSearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.teEcclesia.identity.presentation.shared.components.LookupContentContainer
import teecclesia.designsystem.generated.resources.failed_to_load_educational_stages
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.chips.FilterChip
import com.teEcclesia.designsystem.components.chips.SuggestionChip
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.navigation.BackHandler
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.presentation.screen.requests.toText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.all_roles
import teecclesia.designsystem.generated.resources.all_stages
import teecclesia.designsystem.generated.resources.all_years
import teecclesia.designsystem.generated.resources.close
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.educational_years
import teecclesia.designsystem.generated.resources.filter_users
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_menu
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_search
import teecclesia.designsystem.generated.resources.no_users_found
import teecclesia.designsystem.generated.resources.reset_filters
import teecclesia.designsystem.generated.resources.role
import teecclesia.designsystem.generated.resources.search_users
import teecclesia.designsystem.generated.resources.search_users_hint

@Composable
fun UsersSearchScreen(
    viewModel: UsersSearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UsersSearchContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun UsersSearchContent(
    state: UsersSearchUiState,
    listener: UsersSearchInteractionListener
) {
    BackHandler { listener.onClickBack() }

    val listState = rememberLazyListState()

    FilterBottomSheet(
        state = state,
        listener = listener
    )

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = listener::onRefresh
    ) {
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
                text = stringResource(Res.string.search_users),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        CustomTextField(
            value = state.searchQuery,
            shape = RoundedCornerShape(28.dp),
            onValueChange = listener::onSearchQueryChanged,
            labelText = stringResource(Res.string.search_users_hint),
            leadingIcon = painterResource(Res.drawable.ic_search),
            trailingIcon = painterResource(Res.drawable.ic_menu),
            onTrailingIconClick = { listener.onToggleFilterSheet(true) },
            backgroundColor = Theme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        AnimatedVisibility(
            visible = state.hasActiveFilters,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            ActiveFilterChipsRow(state = state, listener = listener)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (state.isLoading || state.isRefreshing) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else if (state.users.isEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_users_found),
                                style = Theme.typography.bodyLarge,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.users, key = { it.id }) { user ->
                        UserCard(user = user, onClick = { listener.onUserClicked(user) })
                    }

                    if (state.isPagingLoading) {
                        item {
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

        PaginationTrigger(
            list = state.users,
            listState = listState,
            remainingItemsToLoadNextPage = 5,
            loadNextItems = listener::onLoadMore
        )
    }
}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActiveFilterChipsRow(
    state: UsersSearchUiState,
    listener: UsersSearchInteractionListener
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        state.selectedRole?.let { role ->
            ActiveChip(
                text = stringResource(role.toText()),
                onClear = { listener.onRoleFilterSelected(null) }
            )
        }
        state.selectedStage?.let { stage ->
            ActiveChip(
                text = stage.name,
                onClear = if (!state.isStageFilterLocked) { { listener.onStageFilterSelected(null) } } else null
            )
        }
        state.selectedYear?.let { year ->
            ActiveChip(
                text = year.name,
                onClear = if (!state.isYearFilterLocked) { { listener.onYearFilterSelected(null) } } else null
            )
        }
        Text(
            text = stringResource(Res.string.reset_filters),
            style = Theme.typography.labelSmall,
            color = Theme.colorScheme.error,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickableNoRipple { listener.onResetFilters() }
                .padding(4.dp)
        )
    }
}

@Composable
private fun ActiveChip(text: String, onClear: (() -> Unit)?) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colorScheme.primaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = Theme.typography.labelSmall,
                color = Theme.colorScheme.onPrimaryContainer
            )
            if (onClear != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = null,
                    tint = Theme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(14.dp)
                        .clickableNoRipple { onClear() }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UserCard(
    user: ProfileResponse,
    onClick: () -> Unit
) {
    val stageName = user.educationalStageName
    val yearName = user.educationalYearName

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Theme.colorScheme.inverseOnSurface)
            .clickableNoRipple { onClick() }
            .border(1.dp, Theme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (user.imageUrl == null) {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.secondaryContainer)
                        .border(1.dp, Theme.colorScheme.outline, CircleShape)
                        .padding(8.dp)
                )
            } else {
                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.outline)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.fullName,
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.onSecondaryContainer
                )

                Text(
                    text = listOfNotNull(user.phone, user.code).joinToString(" | "),
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.onSecondaryContainer
                )

                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SuggestionChip(
                            label = {
                                Text(
                                    text = stringResource(user.role.toText()),
                                    style = Theme.typography.labelLarge,
                                    color = Theme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        stageName?.let {
                            SuggestionChip(
                                label = {
                                    Text(
                                        text = it,
                                        style = Theme.typography.labelLarge,
                                        color = Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                        yearName?.let {
                            SuggestionChip(
                                label = {
                                    Text(
                                        text = it,
                                        style = Theme.typography.labelLarge,
                                        color = Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Theme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    state: UsersSearchUiState,
    listener: UsersSearchInteractionListener
) {
    BottomSheet(
        isVisible = state.isFilterSheetVisible,
        onDismiss = { listener.onToggleFilterSheet(false) },
        containerColor = Theme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.filter_users),
                    style = Theme.typography.titleLarge,
                    color = Theme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(Res.string.reset_filters),
                    style = Theme.typography.labelMedium,
                    color = Theme.colorScheme.error,
                    modifier = Modifier
                        .clickableNoRipple { listener.onResetFilters() }
                        .padding(4.dp)
                )
            }

            HorizontalDivider()

            Text(
                text = stringResource(Res.string.role),
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSurface
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = state.selectedRole == null,
                    onClick = { listener.onRoleFilterSelected(null) },
                    label = {
                        Text(
                            text = stringResource(Res.string.all_roles),
                            style = Theme.typography.bodyMedium,
                            color = if (state.selectedRole == null) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                state.roles.forEach { role ->
                    FilterChip(
                        selected = state.selectedRole == role,
                        onClick = { listener.onRoleFilterSelected(role) },
                        label = {
                            Text(
                                text = stringResource(role.toText()),
                                style = Theme.typography.bodyMedium,
                                color = if (state.selectedRole == role) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Text(
                text = stringResource(Res.string.educational_stage),
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSurface
            )

            LookupContentContainer(
                isLoading = state.isStageLoading,
                isError = state.isStageLoadFailed,
                isEmpty = state.stages.isEmpty(),
                errorMessage = stringResource(Res.string.failed_to_load_educational_stages),
                onRetry = listener::onRetryLoadStages
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (!state.isStageFilterLocked) {
                        FilterChip(
                            selected = state.selectedStage == null,
                            onClick = { listener.onStageFilterSelected(null) },
                            label = {
                                Text(
                                    text = stringResource(Res.string.all_stages),
                                    style = Theme.typography.bodyMedium,
                                    color = if (state.selectedStage == null) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                    state.stages.forEach { stage ->
                        FilterChip(
                            selected = state.selectedStage?.id == stage.id,
                            onClick = { listener.onStageFilterSelected(stage) },
                            label = {
                                Text(
                                    text = stage.name,
                                    style = Theme.typography.bodyMedium,
                                    color = if (state.selectedStage?.id == stage.id) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.selectedStage != null && state.years.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.educational_years),
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSurface
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (!state.isYearFilterLocked) {
                            FilterChip(
                                selected = state.selectedYear == null,
                                onClick = { listener.onYearFilterSelected(null) },
                                label = {
                                    Text(
                                        text = stringResource(Res.string.all_years),
                                        style = Theme.typography.bodyMedium,
                                        color = if (state.selectedYear == null) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                        state.years.forEach { year ->
                            FilterChip(
                                selected = state.selectedYear?.id == year.id,
                                onClick = { listener.onYearFilterSelected(year) },
                                label = {
                                    Text(
                                        text = year.name,
                                        style = Theme.typography.bodyMedium,
                                        color = if (state.selectedYear?.id == year.id) Theme.colorScheme.onSecondaryContainer else Theme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppButton(
                text = stringResource(Res.string.close),
                onClick = { listener.onToggleFilterSheet(false) },
                type = AppButtonType.Primary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
