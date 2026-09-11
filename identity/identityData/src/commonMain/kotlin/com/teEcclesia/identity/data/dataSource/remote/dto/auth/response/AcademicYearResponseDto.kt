package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcademicYearResponseDto(
    @SerialName("academicYear")
    val academicYear: Int
)
