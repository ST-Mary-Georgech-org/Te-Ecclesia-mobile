package com.teEcclesia.identity.domain.model

data class ApproveUserRequest(
    val customCode: String? = null,
    val updateProfileData: RegisterRequest? = null
)
