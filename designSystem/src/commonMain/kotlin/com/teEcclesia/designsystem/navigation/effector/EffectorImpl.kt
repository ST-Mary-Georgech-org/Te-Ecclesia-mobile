package com.teEcclesia.designsystem.navigation.effector

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class EffectorImpl : Effector {
    private val _effect = Channel<Effect>(Channel.BUFFERED)
    override val effect = _effect.receiveAsFlow()
    private val mutex = Mutex()
    private var lastNavigateTime = 0L
    private var lastPopBackStackTime = 0L
    private var lastPopUpToTime = 0L

    override suspend fun navigate(route: NavKey, forceNavigate: Boolean) {
        mutex.withLock {
            val now = getNow()
            if (forceNavigate || now - lastNavigateTime >= EFFECT_DEBOUNCE_MS) {
                lastNavigateTime = now
                _effect.send(Effect.Navigate(route = route))
            }
        }
    }

    override suspend fun popBackStack() {
        mutex.withLock {
            val now = getNow()
            if (now - lastPopBackStackTime >= EFFECT_DEBOUNCE_MS) {
                lastPopBackStackTime = now
                _effect.send(Effect.PopBackStack)
            }
        }
    }

    override suspend fun resetTo(route: NavKey, forceNavigate: Boolean) {
        mutex.withLock {
            val now = getNow()
            if (forceNavigate || now - lastPopUpToTime >= EFFECT_DEBOUNCE_MS) {
                lastPopUpToTime = now
                _effect.send(
                    Effect.ResetTo(route)
                )
            }
        }
    }

    companion object {
        private const val EFFECT_DEBOUNCE_MS = 300L
    }

    @OptIn(ExperimentalTime::class)
    fun getNow(): Long = Clock.System.now().toEpochMilliseconds()
}
