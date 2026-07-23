package com.teEcclesia.identity.domain.model

import com.teEcclesia.lookups.domain.model.LookupResponse

data class KahenProfileResponse(
    val educationalStages: List<LookupResponse> = emptyList()
)
