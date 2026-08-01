package com.teEcclesia.identity.domain.model

data class CachedProfile(
    val role: UserRole,
    val fullName: String,
    val displayName: String,
    val code: String,
    val imageUrl: String? = null
)
