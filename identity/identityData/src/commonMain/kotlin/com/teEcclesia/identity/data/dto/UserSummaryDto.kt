package com.teEcclesia.identity.data.dto

import com.teEcclesia.identity.domain.model.UserSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSummaryDto(
    @SerialName("id") val id: String,
    @SerialName("code") val code: String? = null,
    @SerialName("name") val name: String,
    @SerialName("imageUrl") val imageUrl: String? = null
)

fun UserSummaryDto.toDomain(): UserSummary {
    return UserSummary(
        id = id,
        code = code,
        name = name,
        imageUrl = imageUrl
    )
}
