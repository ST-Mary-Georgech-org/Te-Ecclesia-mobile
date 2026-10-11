package com.teEcclesia.identity.domain.model

import com.teEcclesia.shared.domain.model.UserRole

data class CompleteProfileRequest(
    val role: UserRole,
    val identityDocumentImageUrl: String? = null,
    val ordinationProfile: OrdinationProfileRequest? = null,
    val makhdoomProfile: MakhdoomProfileRequest? = null,
    val khademProfile: KhademProfileRequest? = null,
    val kahenProfile: KahenProfileRequest? = null,
    val parentProfile: ParentProfileRequest? = null
)
