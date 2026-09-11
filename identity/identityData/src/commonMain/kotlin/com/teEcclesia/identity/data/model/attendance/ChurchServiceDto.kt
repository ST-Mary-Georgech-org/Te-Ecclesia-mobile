package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import com.teEcclesia.shared.domain.utils.getNow
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ChurchServiceDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("responsible")
    val isResponsible: Boolean,
    @SerialName("educationalStages")
    val educationalStages: List<LookupResponseDto> = emptyList(),
    @SerialName("responsibleServants")
    val responsibleServants: List<ResponsibleServantDto>
)

fun ChurchServiceDto.toDomain(): ChurchService {
    return ChurchService(
        id = id,
        name = name,
        createdAt = createdAt.toLocalDateTimeOrDefault(),
        isResponsible = isResponsible,
        educationalStages = educationalStages.map { it.toDomain() },
        responsibleServants = responsibleServants.map { it.toDomain() }
    )
}
