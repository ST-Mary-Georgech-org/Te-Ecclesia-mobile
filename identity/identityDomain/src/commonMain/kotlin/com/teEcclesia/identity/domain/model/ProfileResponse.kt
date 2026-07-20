package com.teEcclesia.identity.domain.model

data class ProfileResponse(
    val id: String,
    val firstName: String,
    val secondName: String,
    val thirdName: String,
    val lastName: String,
    val displayName: String,
    val fullName: String,
    val phone: String,
    val email: String?,
    val isEmailVerified: Boolean,
    val isPhoneVerified: Boolean,
    val imageUrl: String?,
    val specialMark: String,
    val gender: Gender,
    val status: UserStatus,
    val statusReason: String?,
    val role: UserRole,
    val khademProfile: KhademProfileResponse? = null,
    val parentProfile: ParentProfileResponse? = null
)
