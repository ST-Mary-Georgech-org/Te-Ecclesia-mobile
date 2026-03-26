package com.teEcclesia.appEntryPoint

import com.teEcclesia.designsystem.components.snackbar.SnackBarData

data class MainEntryState(
    val activeFeature: Feature = Feature.Home,
    val showBottomNavigation: Boolean = true,
    val isSnackBarVisible: Boolean = false,
    val snackBarData: SnackBarData = SnackBarData(""),
)

enum class Feature {
    Home, Categories, Stats, ChatBot, Profile, Payments
}