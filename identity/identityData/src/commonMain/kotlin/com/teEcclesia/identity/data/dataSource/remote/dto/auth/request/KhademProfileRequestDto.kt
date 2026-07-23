package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.KhademProfileRequest

@Serializable
data class KhademProfileRequestDto(
    @SerialName("educationalStageId")
    val educationalStageId: Long,
    @SerialName("educationalYearId")
    val educationalYearId: Long? = null
)

fun KhademProfileRequest.toDto() = KhademProfileRequestDto(
    educationalStageId = educationalStageId,
    educationalYearId = educationalYearId
)
