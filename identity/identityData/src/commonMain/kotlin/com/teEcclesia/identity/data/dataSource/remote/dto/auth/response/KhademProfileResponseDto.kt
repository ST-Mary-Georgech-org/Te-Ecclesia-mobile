package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.KhademProfileResponse
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KhademProfileResponseDto(
    @SerialName("educationalStage")
    val educationalStage: LookupResponseDto,
    @SerialName("educationalYear")
    val educationalYear: LookupResponseDto? = null
)

fun KhademProfileResponseDto.toDomain() = KhademProfileResponse(
    educationalStage = educationalStage.toDomain(),
    educationalYear = educationalYear?.toDomain()
)
