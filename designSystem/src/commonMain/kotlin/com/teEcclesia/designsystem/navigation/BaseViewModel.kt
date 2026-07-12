package com.teEcclesia.designsystem.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.designsystem.utils.pagination.Paginator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModel<STATE>(
    initialState: STATE
) : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    private val effector: Effector by inject()
    protected val snackBarManager: SnackBarManager by inject()
    private val resultStore: ResultStore by inject()
    protected val dispatchers: DispatcherProvider by inject()

    protected fun navigate(
        route: NavKey,
        forceNavigate: Boolean = false
    ) =
        viewModelScope.launch {
            effector.navigate(
                route = route,
                forceNavigate = forceNavigate
            )
        }

    protected fun popBackStack(vararg arguments: Pair<String, Any>) =
        viewModelScope.launch {
            resultStore.setResults(arguments.toMap())
            effector.popBackStack()
        }

    protected fun <T> getResult(key: String, consume: Boolean): Flow<T?> =
        resultStore.getResult(key, consume)

    protected fun resetTo(
        route: NavKey,
        forceNavigate: Boolean = false
    ) {
        viewModelScope.launch { effector.resetTo(route, forceNavigate) }
    }

    protected fun showSnackBar(
        title: UiText,
        message: UiText? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    ) {
        snackBarManager.showSnackBar(
            title = title,
            message = message,
            isSuccess = isSuccess,
            customLeadingIcon = customLeadingIcon,
            duration = duration,
            iconTint = iconTint
        )
    }

    fun updateState(transform: STATE.(STATE) -> STATE) {
        _state.update { it.transform(it) }
    }

    protected fun <R> tryToCall(
        block: suspend () -> R,
        onSuccess: suspend (R) -> Unit,
        onError: (Throwable) -> Unit,
        onStart: suspend () -> Unit = {},
        onEnd: suspend () -> Unit = {},
        dispatcher: CoroutineDispatcher = dispatchers.io
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable !is CancellationException) {
                onError(throwable)
            }
        }
        return viewModelScope.launch(dispatcher + exceptionHandler) {
            onStart()
            try {
                val result = block()
                onSuccess(result)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                onError(e)
            } finally {
                onEnd()
            }
        }
    }

    protected fun <R> tryToCollect(
        block: () -> Flow<R>,
        onStart: () -> Unit = {},
        onEach: (R) -> Unit,
        onError: (Throwable) -> Unit,
        onEnd: () -> Unit = {},
        dispatcher: CoroutineDispatcher = dispatchers.io,
        scope: CoroutineScope = viewModelScope
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            if (exception !is CancellationException) {
                onError(exception)
            }
        }
        return scope.launch(dispatcher + exceptionHandler) {
            block()
                .flowOn(dispatcher)
                .onStart { onStart() }
                .onEach { onEach(it) }
                .onCompletion { throwable ->
                    if (throwable != null) {
                        if (throwable !is CancellationException) {
                            onError(throwable)
                        }
                    } else {
                        onEnd()
                    }
                }
                .catch { throwable ->
                    if (throwable !is CancellationException) {
                        onError(throwable)
                    } else {
                        throw throwable
                    }
                }
                .collect()
        }
    }

    protected fun <Key, Items> createPaginator(
        initialKey: Key,
        pageSize: Int,
        loadPage: suspend (pageNumber: Key) -> List<Items>,
        onSuccess: (items: List<Items>) -> Unit,
        onLoadUpdated: (Boolean) -> Unit,
        onError: (Throwable?) -> Unit = {},
        endReached: (items: List<Items>, pageSize: Int) -> Boolean = { items, size ->
            items.isEmpty() || items.size < size
        }
    ): Paginator<Key, List<Items>> {
        return Paginator(
            initialKey = initialKey,
            onLoadUpdated = onLoadUpdated,
            onRequest = { pageNumber -> loadPage(pageNumber) },
            getNextKey = { currentKey, _ ->
                @Suppress("UNCHECKED_CAST")
                when (currentKey) {
                    is Int -> (currentKey + 1) as Key
                    else -> currentKey
                }
            },
            onError = { throwable ->
                onError(throwable)
            },
            onSuccess = { items, _ -> onSuccess(items) },
            endReached = { _, result -> endReached(result, pageSize) }
        )
    }
}
