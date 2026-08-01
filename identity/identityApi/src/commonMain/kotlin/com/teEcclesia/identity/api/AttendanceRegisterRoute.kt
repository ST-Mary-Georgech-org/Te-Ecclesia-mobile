package com.teEcclesia.identity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRegisterRoute(
    val eventId: Long,
    val serviceName: String,
    val eventName: String
) : NavKey
