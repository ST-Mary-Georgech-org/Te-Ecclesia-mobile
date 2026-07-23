package com.teEcclesia.identity.domain.model

data class ParentProfileRequest(
    val partnerCode: String? = null,
    val childrenCodes: List<String>? = emptyList(),
    val nationalIdImageUrl: String? = null
)
