package com.teEcclesia.identity.domain.model

data class CompleteProfileRequest(
    val role: UserRole,
    val ordinationProfile: OrdinationProfileRequest? = null,
    val makhdoomProfile: MakhdoomProfileRequest? = null,
    val khademProfile: KhademProfileRequest? = null,
    val parentProfile: ParentProfileRequest? = null
)
