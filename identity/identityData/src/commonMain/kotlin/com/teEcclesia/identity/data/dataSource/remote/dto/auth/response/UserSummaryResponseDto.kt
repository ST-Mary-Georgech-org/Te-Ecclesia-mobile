package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.UserSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSummaryResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("code")
    val code: String?,
    @SerialName("imageUrl")
    val imageUrl: String?
)

fun UserSummaryResponseDto.toDomain() = UserSummary(
    id = id,
    name = fullName,
    code = code,
    imageUrl = imageUrl
)
