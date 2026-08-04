package com.teEcclesia.designsystem.utils.pagination

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Paginator<Key, Items>(
    private val scope: CoroutineScope,
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
    private var job: Job? = null

    fun loadNextItems(): Job? {
        if (isMakingRequest || isEndReached || isErrorState) return null

        isMakingRequest = true
        onLoadUpdated(true)

        job = scope.launch(exceptionHandler) {
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
            } catch (e: Throwable) {
                isMakingRequest = false
                isErrorState = true
                onError(e)
                onLoadUpdated(false)
            }
        }
        return job
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) {
            isMakingRequest = false
            isErrorState = true
            onLoadUpdated(false)
        }
    }

    fun reset() {
        job?.cancel()
        currentKey = initialKey
        isEndReached = false
        isErrorState = false
        isMakingRequest = false
        onReset()
        loadNextItems()
    }
}
