package com.teEcclesia.designsystem.navigation.effector

import androidx.navigation3.runtime.NavKey

sealed class Effect {
    data class Navigate(val route: NavKey) : Effect()
    data object PopBackStack : Effect()
    data class ResetTo(val route: NavKey) : Effect()
}
