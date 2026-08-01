package com.teEcclesia.identity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceEventsRoute(
    val serviceId: Long,
    val serviceName: String
) : NavKey
