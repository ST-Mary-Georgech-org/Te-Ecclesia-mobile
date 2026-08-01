package com.teEcclesia.identity.domain.model

data class CachedProfile(
    val role: com.teEcclesia.shared.domain.model.UserRole,
    val fullName: String,
    val displayName: String,
    val code: String,
    val imageUrl: String? = null
)
