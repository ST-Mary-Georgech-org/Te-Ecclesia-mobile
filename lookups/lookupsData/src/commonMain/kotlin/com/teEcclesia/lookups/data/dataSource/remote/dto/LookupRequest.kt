package com.teEcclesia.lookups.data.dataSource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LookupRequest(
    val name: String
)
