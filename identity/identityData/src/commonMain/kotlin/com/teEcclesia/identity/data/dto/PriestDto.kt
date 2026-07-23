package com.teEcclesia.identity.data.dto

import com.teEcclesia.identity.domain.model.Priest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriestDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String
)

fun PriestDto.toDomain(): Priest {
    return Priest(
        id = id,
        name = name
    )
}
