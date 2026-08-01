package com.teEcclesia.identity.domain.model

data class CompleteProfileRequest(
    val role: com.teEcclesia.shared.domain.model.UserRole,
    val ordinationProfile: OrdinationProfileRequest? = null,
    val makhdoomProfile: MakhdoomProfileRequest? = null,
    val khademProfile: KhademProfileRequest? = null,
    val kahenProfile: KahenProfileRequest? = null,
    val parentProfile: ParentProfileRequest? = null
)
