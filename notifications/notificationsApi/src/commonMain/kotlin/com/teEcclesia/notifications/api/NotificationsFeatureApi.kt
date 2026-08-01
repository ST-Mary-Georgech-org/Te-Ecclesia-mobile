package com.teEcclesia.notifications.api

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

@Stable
interface NotificationsFeatureApi {
    operator fun invoke(): (NavKey) -> NavEntry<NavKey>
}
