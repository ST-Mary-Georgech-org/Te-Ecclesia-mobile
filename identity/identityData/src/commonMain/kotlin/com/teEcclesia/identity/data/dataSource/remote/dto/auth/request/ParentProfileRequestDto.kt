package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.ParentProfileRequest

@Serializable
data class ParentProfileRequestDto(
    @SerialName("partnerCode")
    val partnerCode: String? = null,
    @SerialName("childrenCodes")
    val childrenCodes: List<String>? = emptyList(),
    @SerialName("nationalIdImageUrl")
    val nationalIdImageUrl: String? = null
)

fun ParentProfileRequest.toDto() = ParentProfileRequestDto(
    partnerCode = partnerCode?.ifEmpty { null },
    childrenCodes = childrenCodes?.takeIf { it.isNotEmpty() },
    nationalIdImageUrl = nationalIdImageUrl?.ifEmpty { null }
)
