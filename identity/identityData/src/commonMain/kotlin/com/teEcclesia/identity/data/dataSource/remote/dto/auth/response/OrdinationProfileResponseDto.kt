package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.OrdinationProfileResponse
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrdinationProfileResponseDto(
    @SerialName("rank")
    val rank: LookupResponseDto,
    @SerialName("ordinationInAnotherChurch")
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

fun OrdinationProfileResponseDto.toDomain() = OrdinationProfileResponse(
    rank = rank.toDomain(),
    isOrdinationInAnotherChurch = isOrdinationInAnotherChurch,
    ordinationYear = ordinationYear,
    bishopName = bishopName.orEmpty(),
    ordinationPlace = ordinationPlace.orEmpty(),
    certificateImageUrl = certificateImageUrl
)
