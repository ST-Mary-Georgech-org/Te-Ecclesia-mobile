package com.teEcclesia.identity.presentation.navigation.effector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavOptions
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.presentation.navigation.BaseRoute
import kotlinx.coroutines.flow.Flow

interface Effector {
    val effect: Flow<Effect>
    val backStackArgsFlow: Flow<Map<String, Any>>

    suspend fun navigate(
        route: BaseRoute,
        navOptions: NavOptions? = null,
        forceNavigate: Boolean = false
    )

    suspend fun showSnackBar(
        title: UiText,
        message: UiText? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    )

    suspend fun popBackStack(vararg arguments: Pair<String, Any>)
    suspend fun popUpTo(route: BaseRoute, inclusive: Boolean = false, saveState: Boolean = false)
    suspend fun setBackStackArgs(vararg arguments: Pair<String, Any>)
    suspend fun updateBottomNavigationVisibility(isVisible: Boolean)
}