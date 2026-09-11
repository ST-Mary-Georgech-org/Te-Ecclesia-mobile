package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.data.dto.UserSummaryDto
import com.teEcclesia.identity.data.dto.toDomain
import com.teEcclesia.identity.domain.model.ParentProfileResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentProfileResponseDto(
    @SerialName("partner")
    val partner: UserSummaryDto?,
    @SerialName("children")
    val children: List<UserSummaryDto>,
    @SerialName("nationalIdImageUrl")
    val nationalIdImageUrl: String? = null,
    @SerialName("whatsAppLink")
    val whatsAppLink: String? = null
)

fun ParentProfileResponseDto.toDomain() = ParentProfileResponse(
    partner = partner?.toDomain(),
    children = children.map { it.toDomain() },
    nationalIdImageUrl = nationalIdImageUrl,
    whatsAppLink = whatsAppLink
)
