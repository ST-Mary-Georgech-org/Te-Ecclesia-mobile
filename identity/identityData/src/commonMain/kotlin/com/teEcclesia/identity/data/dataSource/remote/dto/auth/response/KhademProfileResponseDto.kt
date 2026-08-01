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
    val educationalYear: LookupResponseDto? = null,
    @SerialName("canApproveRequests")
    val canApproveRequests: Boolean = false,
    @SerialName("responsibleStages")
    val responsibleStages: List<LookupResponseDto> = emptyList(),
    @SerialName("responsibleYears")
    val responsibleYears: List<LookupResponseDto> = emptyList()
)

fun KhademProfileResponseDto.toDomain() = KhademProfileResponse(
    educationalStage = educationalStage.toDomain(),
    educationalYear = educationalYear?.toDomain(),
    canApproveRequests = canApproveRequests,
    responsibleStages = responsibleStages.map { it.toDomain() },
    responsibleYears = responsibleYears.map { it.toDomain() }
)
