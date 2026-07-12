package com.teEcclesia.designsystem.navigation.effector

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.Flow

interface Effector {
    val effect: Flow<Effect>

    suspend fun navigate(route: NavKey, forceNavigate: Boolean = false)
    suspend fun popBackStack()
    suspend fun resetTo(route: NavKey, forceNavigate: Boolean = false)
}
