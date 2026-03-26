package com.teEcclesia.identity.presentation.shared

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.presentation.navigation.BaseRoute
import com.teEcclesia.identity.presentation.navigation.effector.Effector
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

    protected val backStackArgsFlow = effector.backStackArgsFlow

    protected fun navigate(
        route: BaseRoute,
        forceNavigate: Boolean = false,
        navOptions: NavOptions? = null,
    ) =
        viewModelScope.launch {
            effector.navigate(route = route, navOptions = navOptions, forceNavigate = forceNavigate)
        }

    protected fun setNavigationArgs(vararg arguments: Pair<String, Any>) =
        viewModelScope.launch { effector.setBackStackArgs(*arguments) }

    protected fun popBackStack(vararg arguments: Pair<String, Any>) =
        viewModelScope.launch { effector.popBackStack(*arguments) }

    protected fun updateBottomNavigationVisibility(isVisible: Boolean) =
        viewModelScope.launch { effector.updateBottomNavigationVisibility(isVisible) }

    protected fun popUpTo(
        route: BaseRoute,
        inclusive: Boolean = false,
        saveState: Boolean = false
    ) {
        viewModelScope.launch { effector.popUpTo(route, inclusive, saveState) }
    }

    protected fun showSnackBar(
        title: UiText,
        message: UiText? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    ) = viewModelScope.launch {
        effector.showSnackBar(
            title, message, isSuccess, customLeadingIcon, duration, iconTint
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
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
        return viewModelScope.launch(dispatcher + exceptionHandler) {
            onStart()
            runCatching { block() }
                .onSuccess { onSuccess(it) }
                .onFailure { onError(it) }
            onEnd()
        }
    }

    protected fun <R> tryToCollect(
        block: () -> Flow<R>,
        onStart: () -> Unit = {},
        onEach: (R) -> Unit,
        onError: (Throwable) -> Unit,
        onEnd: () -> Unit = {},
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        scope: CoroutineScope = viewModelScope
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, exception -> onError(exception) }
        return scope.launch(dispatcher + exceptionHandler) {
            block()
                .flowOn(dispatcher)
                .onStart { onStart() }
                .onEach { onEach(it) }
                .onCompletion { throwable ->
                    throwable?.let {
                        onError(throwable)
                    } ?: onEnd()
                }
                .catch { throwable -> onError(throwable) }
                .collect()
        }
    }

}