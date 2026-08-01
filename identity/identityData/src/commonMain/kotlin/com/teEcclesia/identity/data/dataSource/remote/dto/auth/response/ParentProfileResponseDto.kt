package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.ParentProfileResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentProfileResponseDto(
    @SerialName("partner")
    val partner: UserSummaryResponseDto?,
    @SerialName("children")
    val children: List<UserSummaryResponseDto>,
    @SerialName("nationalIdImageUrl")
    val nationalIdImageUrl: String? = null
)

fun ParentProfileResponseDto.toDomain() = ParentProfileResponse(
    partner = partner?.toDomain(),
    children = children.map { it.toDomain() },
    nationalIdImageUrl = nationalIdImageUrl
)
