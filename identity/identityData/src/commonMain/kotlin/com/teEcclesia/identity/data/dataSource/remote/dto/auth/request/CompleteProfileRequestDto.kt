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
    @SerialName("kahenProfile")
    val kahenProfile: KahenProfileRequestDto? = null,
    @SerialName("parentProfile")
    val parentProfile: ParentProfileRequestDto? = null,
    @SerialName("deviceToken")
    val deviceToken: String? = null
)

fun CompleteProfileRequest.toDto(deviceToken: String? = null) = CompleteProfileRequestDto(
    role = role,
    ordinationProfile = ordinationProfile?.toDto(),
    makhdoomProfile = makhdoomProfile?.toDto(),
    khademProfile = khademProfile?.toDto(),
    kahenProfile = kahenProfile?.toDto(),
    parentProfile = parentProfile?.toDto(),
    deviceToken = deviceToken
)
