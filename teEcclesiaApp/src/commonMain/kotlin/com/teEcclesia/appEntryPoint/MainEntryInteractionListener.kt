package com.teEcclesia.appEntryPoint

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

interface MainEntryInteractionListener {
    fun onBottomNavigationChanged(isShowed: Boolean)
    fun setActiveFeature(feature: Feature)
    fun showSnackBar(
        title: String,
        message: String? = null,
        isSuccess: Boolean = true,
        customLeadingIcon: Painter? = null,
        duration: Long? = null,
        iconTint: Color = Color.Unspecified
    )

    fun hideSnackBar()
}