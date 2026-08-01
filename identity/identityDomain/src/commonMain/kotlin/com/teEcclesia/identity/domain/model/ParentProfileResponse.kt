package com.teEcclesia.identity.domain.model

data class ParentProfileResponse(
    val partner: UserSummary?,
    val children: List<UserSummary>,
    val nationalIdImageUrl: String? = null
)
