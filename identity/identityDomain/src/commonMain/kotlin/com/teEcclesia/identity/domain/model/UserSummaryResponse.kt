package com.teEcclesia.identity.domain.model

data class UserSummaryResponse(
    val id: String,
    val fullName: String,
    val code: String?,
    val imageUrl: String?
)
