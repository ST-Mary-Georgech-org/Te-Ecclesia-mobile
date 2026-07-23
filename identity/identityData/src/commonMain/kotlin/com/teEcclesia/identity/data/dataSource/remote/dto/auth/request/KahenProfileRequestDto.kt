package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.KahenProfileRequest

@Serializable
data class KahenProfileRequestDto(
    @SerialName("educationalStageIds")
    val educationalStageIds: List<Long>
)

fun KahenProfileRequest.toDto() = KahenProfileRequestDto(
    educationalStageIds = educationalStageIds
)
