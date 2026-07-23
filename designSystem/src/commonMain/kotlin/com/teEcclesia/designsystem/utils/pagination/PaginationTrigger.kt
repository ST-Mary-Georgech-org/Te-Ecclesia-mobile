package com.teEcclesia.designsystem.utils.pagination

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun <T> PaginationTrigger(
    list: List<T>,
    listState: LazyListState,
    remainingItemsToLoadNextPage: Int,
    loadNextItems: () -> Unit
) {
    LaunchedEffect(list) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= list.size - remainingItemsToLoadNextPage && list.isNotEmpty()) {
                    loadNextItems()
                }
            }
    }
}

@Composable
fun <T> PaginationTrigger(
    list: List<T>,
    listState: LazyGridState,
    remainingItemsToLoadNextPage: Int,
    loadNextItems: () -> Unit
) {
    LaunchedEffect(list) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= list.size - remainingItemsToLoadNextPage && list.isNotEmpty()) {
                    loadNextItems()
                }
            }
    }
}
