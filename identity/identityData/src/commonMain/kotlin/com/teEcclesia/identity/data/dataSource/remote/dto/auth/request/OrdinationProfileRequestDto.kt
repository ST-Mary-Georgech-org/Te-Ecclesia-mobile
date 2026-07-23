package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.OrdinationProfileRequest

@Serializable
data class OrdinationProfileRequestDto(
    @SerialName("rankId")
    val rankId: Long,
    @SerialName("isOrdinationInAnotherChurch")
    val isOrdinationInAnotherChurch: Boolean,
    @SerialName("ordinationYear")
    val ordinationYear: Int? = null,
    @SerialName("bishopName")
    val bishopName: String? = null,
    @SerialName("ordinationPlace")
    val ordinationPlace: String? = null,
    @SerialName("certificateImageUrl")
    val certificateImageUrl: String? = null
)

fun OrdinationProfileRequest.toDto() = OrdinationProfileRequestDto(
    rankId = rankId,
    isOrdinationInAnotherChurch = isOrdinationInAnotherChurch,
    ordinationYear = ordinationYear,
    bishopName = bishopName?.ifEmpty { null },
    ordinationPlace = ordinationPlace?.ifEmpty { null },
    certificateImageUrl = certificateImageUrl?.ifEmpty { null }
)
