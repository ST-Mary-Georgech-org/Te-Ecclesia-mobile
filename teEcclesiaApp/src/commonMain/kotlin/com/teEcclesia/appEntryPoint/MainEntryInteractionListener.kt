package com.teEcclesia.appEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation3.runtime.NavKey

interface MainEntryInteractionListener {
    fun onBottomNavigationChanged(isShowed: Boolean)
    fun showSnackBar(
        title: String,
        message: String? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    )

    fun hideSnackBar()

    fun resetToRoute(route: NavKey, forceNavigate: Boolean = false)
    fun navigateToRoute(route: NavKey, forceNavigate: Boolean = false)
}
