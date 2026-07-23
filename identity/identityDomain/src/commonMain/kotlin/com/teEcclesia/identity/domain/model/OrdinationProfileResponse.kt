package com.teEcclesia.identity.domain.model

import com.teEcclesia.lookups.domain.model.LookupResponse

data class OrdinationProfileResponse(
    val rank: LookupResponse,
    val isOrdinationInAnotherChurch: Boolean,
    val ordinationYear: Int?,
    val bishopName: String,
    val ordinationPlace: String,
    val certificateImageUrl: String?
)
