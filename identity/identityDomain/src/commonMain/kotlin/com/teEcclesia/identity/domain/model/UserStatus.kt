package com.teEcclesia.identity.domain.model

enum class UserStatus {
    PROFILE_INCOMPLETE,
    UNVERIFIED,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    SUSPENDED
}
