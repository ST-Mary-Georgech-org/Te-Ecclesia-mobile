package com.teEcclesia.identity.data.dto

import com.teEcclesia.identity.domain.model.UserSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSummaryDto(
    @SerialName("id") val id: String,
    @SerialName("code") val code: String? = null,
    @SerialName("fullName") val fullName: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("displayName") val displayName: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null
)

fun UserSummaryDto.toDomain(): UserSummary {
    return UserSummary(
        id = id,
        code = code,
        name = fullName ?: name ?: displayName ?: "",
        imageUrl = imageUrl
    )
}
