package com.teEcclesia.identity.domain.model

data class ParentProfileResponse(
    val partner: UserSummaryResponse?,
    val children: List<UserSummaryResponse>
)
