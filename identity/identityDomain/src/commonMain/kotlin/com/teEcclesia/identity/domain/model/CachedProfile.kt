package com.teEcclesia.identity.domain.model

import com.teEcclesia.shared.domain.model.UserRole

data class CachedProfile(
    val role: UserRole,
    val fullName: String,
    val displayName: String,
    val code: String,
    val imageUrl: String? = null,
    val whatsAppLink: String?
)
