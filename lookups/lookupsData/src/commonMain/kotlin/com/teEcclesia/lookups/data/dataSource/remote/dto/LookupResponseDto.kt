package com.teEcclesia.lookups.data.dataSource.remote.dto

import com.teEcclesia.lookups.domain.model.LookupResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookupResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("subItems")
    val subItems: List<LookupResponseDto>? = null,
    @SerialName("isKhademOnly")
    val isKhademOnly: Boolean = false,
    @SerialName("whatsAppLink")
    val whatsAppLink: String? = null
)

fun LookupResponseDto.toDomain(): LookupResponse = LookupResponse(
    id = id,
    name = name,
    subItems = subItems?.map(LookupResponseDto::toDomain) ?: emptyList(),
    isKhademOnly = isKhademOnly,
    whatsAppLink = whatsAppLink
)
