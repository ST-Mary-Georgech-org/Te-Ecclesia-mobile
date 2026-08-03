package com.teEcclesia.designsystem.utils.pagination

import kotlinx.coroutines.CancellationException

class Paginator<Key, Items>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Items,
    private val getNextKey: suspend (currentKey: Key, result: Items) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (result: Items, newKey: Key) -> Unit,
    private val endReached: (currentKey: Key, result: Items) -> Boolean,
    private val onReset: () -> Unit = {}
) {

    private var currentKey = initialKey
    private var isMakingRequest = false
    private var isEndReached = false
    private var isErrorState = false

    suspend fun loadNextItems() {
        if (isMakingRequest || isEndReached || isErrorState) return

        isMakingRequest = true
        onLoadUpdated(true)

        try {
            val items = onRequest(currentKey)
            isMakingRequest = false
            currentKey = getNextKey(currentKey, items)
            onSuccess(items, currentKey)
            onLoadUpdated(false)
            isEndReached = endReached(currentKey, items)
        } catch (e: CancellationException) {
            isMakingRequest = false
            onLoadUpdated(false)
            throw e
        } catch (e: Exception) {
            isMakingRequest = false
            isErrorState = true
            onError(e)
            onLoadUpdated(false)
        }
    }

    suspend fun reset() {
        currentKey = initialKey
        isEndReached = false
        isErrorState = false
        isMakingRequest = false
        onReset()
        loadNextItems()
    }
}
