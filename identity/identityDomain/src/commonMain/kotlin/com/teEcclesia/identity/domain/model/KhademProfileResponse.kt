package com.teEcclesia.identity.domain.model

import com.teEcclesia.lookups.domain.model.LookupResponse

data class KhademProfileResponse(
    val educationalStage: LookupResponse,
    val educationalYear: LookupResponse?,
    val canApproveRequests: Boolean = false
)
