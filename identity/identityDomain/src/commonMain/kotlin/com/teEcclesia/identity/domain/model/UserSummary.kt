package com.teEcclesia.identity.domain.model

data class UserSummary(
    val id: String,
    val code: String?,
    val name: String,
    val imageUrl: String?
)
