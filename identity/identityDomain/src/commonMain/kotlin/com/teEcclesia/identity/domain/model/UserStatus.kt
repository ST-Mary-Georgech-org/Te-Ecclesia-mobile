package com.teEcclesia.identity.domain.model

enum class UserStatus {
    UNVERIFIED,
    PROFILE_INCOMPLETE,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    BANNED
}
