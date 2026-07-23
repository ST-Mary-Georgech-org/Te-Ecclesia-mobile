package com.teEcclesia.home.api

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

@Stable
interface HomeFeatureApi {
    operator fun invoke(): (NavKey) -> NavEntry<NavKey>
}
