package com.teEcclesia.designsystem.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Stable
class ResultStore {
    private val results = mutableStateMapOf<Any, Any?>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getResult(key: Any, consume: Boolean): Flow<T?> =
        snapshotFlow { results[key] }
            .map { value ->
                if (consume && value != null) {
                    results.remove(key)
                }
                value as? T
            }
            .distinctUntilChanged()

    fun <T> setResult(key: Any, value: T) {
        results[key] = value
    }

    fun <T> setResults(resultsMap: Map<Any, T>) {
        results.putAll(resultsMap)
    }

    fun removeResult(key: Any) {
        results.remove(key)
    }

    companion object {
        val Saver = Saver<ResultStore, Map<Any, Any?>>(
            save = { it.results.toMap() },
            restore = {
                ResultStore().apply {
                    results.putAll(it)
                }
            }
        )
    }
}

@Composable
fun rememberResultStore() = rememberSaveable(
    saver = ResultStore.Saver
) {
    ResultStore()
}
