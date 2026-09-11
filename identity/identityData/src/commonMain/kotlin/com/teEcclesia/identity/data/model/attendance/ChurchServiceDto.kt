package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ChurchService
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
    @SerialName("educationalStageId")
    val educationalStageId: Long?,
    @SerialName("educationalStageName")
    val educationalStageName: String?,
    @SerialName("responsibleServants")
    val responsibleServants: List<ResponsibleServantDto>
)

fun ChurchServiceDto.toDomain(): ChurchService {
    return ChurchService(
        id = id,
        name = name,
        createdAt = createdAt.toLocalDateTimeOrDefault(),
        isResponsible = isResponsible,
        educationalStageId = educationalStageId,
        educationalStageName = educationalStageName,
        responsibleServants = responsibleServants.map { it.toDomain() }
    )
}
