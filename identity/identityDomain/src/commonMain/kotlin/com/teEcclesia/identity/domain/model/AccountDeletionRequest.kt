package com.teEcclesia.identity.domain.model

import com.teEcclesia.shared.domain.model.UserRole
import kotlinx.datetime.LocalDateTime

data class AccountDeletionRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val userCode: String?,
    val userImageUrl: String?,
    val userRole: UserRole,
    val reason: String,
    val requestedAt: LocalDateTime
)
