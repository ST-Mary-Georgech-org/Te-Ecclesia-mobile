package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.KahenProfileResponse
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain

@Serializable
data class KahenProfileResponseDto(
    @SerialName("educationalStages")
    val educationalStages: List<LookupResponseDto> = emptyList()
)

fun KahenProfileResponseDto.toDomain() = KahenProfileResponse(
    educationalStages = educationalStages.map { it.toDomain() }
)
