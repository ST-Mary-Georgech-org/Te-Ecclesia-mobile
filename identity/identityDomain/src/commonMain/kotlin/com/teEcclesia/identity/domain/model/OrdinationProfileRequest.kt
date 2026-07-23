package com.teEcclesia.identity.domain.model

data class OrdinationProfileRequest(
    val rankId: Long,
    val isOrdinationInAnotherChurch: Boolean,
    val ordinationYear: Int? = null,
    val bishopName: String? = null,
    val ordinationPlace: String? = null,
    val certificateImageUrl: String? = null
)
