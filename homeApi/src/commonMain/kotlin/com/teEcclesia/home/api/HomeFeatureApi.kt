package com.teEcclesia.home.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
interface HomeFeatureApi {
    @Composable
    fun TabEntry(updateBottomNavigationVisibility: (Boolean) -> Unit)
}