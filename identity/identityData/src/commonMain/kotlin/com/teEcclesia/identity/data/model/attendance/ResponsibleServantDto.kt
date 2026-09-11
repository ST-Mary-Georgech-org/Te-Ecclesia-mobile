package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.identity.domain.model.attendance.ResponsibleServant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsibleServantDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String?,
    @SerialName("imageUrl")
    val imageUrl: String?
)

fun ResponsibleServantDto.toDomain(): ResponsibleServant {
    return ResponsibleServant(
        id = id,
        name = name,
        code = code,
        imageUrl = imageUrl
    )
}
