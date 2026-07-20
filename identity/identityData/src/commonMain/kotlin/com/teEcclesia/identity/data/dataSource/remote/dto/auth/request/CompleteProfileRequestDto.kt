package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.CompleteProfileRequest
import com.teEcclesia.identity.domain.model.UserRole

@Serializable
data class CompleteProfileRequestDto(
    @SerialName("role")
    val role: UserRole,
    @SerialName("ordinationProfile")
    val ordinationProfile: OrdinationProfileRequestDto? = null,
    @SerialName("makhdoomProfile")
    val makhdoomProfile: MakhdoomProfileRequestDto? = null,
    @SerialName("khademProfile")
    val khademProfile: KhademProfileRequestDto? = null,
    @SerialName("parentProfile")
    val parentProfile: ParentProfileRequestDto? = null
)

fun CompleteProfileRequest.toDto() = CompleteProfileRequestDto(
    role = role,
    ordinationProfile = ordinationProfile?.toDto(),
    makhdoomProfile = makhdoomProfile?.toDto(),
    khademProfile = khademProfile?.toDto(),
    parentProfile = parentProfile?.toDto()
)
