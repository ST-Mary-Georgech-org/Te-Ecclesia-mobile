package com.teEcclesia.identity.domain.model.attendance

import com.teEcclesia.shared.domain.model.UserRole

data class AttendeeUserPreview(
    val id: String,
    val name: String,
    val role: UserRole,
    val stageName: String?,
    val yearName: String?,
    val code: String?,
    val imageUrl: String?
)
