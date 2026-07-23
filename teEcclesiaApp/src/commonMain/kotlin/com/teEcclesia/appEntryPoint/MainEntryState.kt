package com.teEcclesia.appEntryPoint

import com.teEcclesia.designsystem.components.snackbar.SnackBarData

data class MainEntryState(
    val showBottomNavigation: Boolean = true,
    val isSnackBarVisible: Boolean = false,
    val snackBarData: SnackBarData = SnackBarData(""),
)
