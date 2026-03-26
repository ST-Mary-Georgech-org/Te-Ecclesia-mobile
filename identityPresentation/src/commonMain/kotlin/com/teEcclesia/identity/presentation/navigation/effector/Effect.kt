package com.teEcclesia.identity.presentation.navigation.effector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavOptions
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.presentation.navigation.BaseRoute

sealed class Effect {
    data class Navigate(val route: BaseRoute, val navOptions: NavOptions? = null) : Effect()
    data class PopBackStack(val arguments: Map<String, Any> = emptyMap()) : Effect()
    data class PopUpTo(
        val route: BaseRoute,
        val inclusive: Boolean = false,
        val saveState: Boolean = false
    ) : Effect()
    data class ShowSnackBar(
        val title: UiText,
        val message: UiText? = null,
        val isSuccess: Boolean = true,
        val customLeadingIcon: Painter? = null,
        val duration: Long? = null,
        val iconTint: Color = Color.Unspecified
    ) : Effect()
    data class SetBackStackArgs(val arguments: Map<String, Any> = emptyMap()) : Effect()
    data class UpdateBottomNavigationVisibility(val isVisible: Boolean) : Effect()
}