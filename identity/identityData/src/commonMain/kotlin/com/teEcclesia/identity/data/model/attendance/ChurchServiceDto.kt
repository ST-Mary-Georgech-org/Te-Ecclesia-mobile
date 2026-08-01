package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ChurchService
import com.teEcclesia.shared.domain.utils.getNow
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChurchServiceDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("createdAt")
    val createdAt: String? = null
)

fun ChurchServiceDto.toDomain(): ChurchService {
    return ChurchService(
        id = id,
        name = name,
        createdAt = createdAt?.toLocalDateTimeOrDefault() ?: getNow()
    )
}
