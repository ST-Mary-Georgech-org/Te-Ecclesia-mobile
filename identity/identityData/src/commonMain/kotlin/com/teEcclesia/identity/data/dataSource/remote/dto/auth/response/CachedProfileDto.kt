package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.CachedProfile
import com.teEcclesia.identity.domain.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachedProfileDto(
    @SerialName("role")
    val role: UserRole,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("code")
    val code: String,
    @SerialName("image_url")
    val imageUrl: String? = null
)

fun CachedProfileDto.toDomain(): CachedProfile = CachedProfile(
    role = role,
    fullName = fullName,
    displayName = displayName,
    code = code,
    imageUrl = imageUrl
)

fun CachedProfile.toDto(): CachedProfileDto = CachedProfileDto(
    role = role,
    fullName = fullName,
    displayName = displayName,
    code = code,
    imageUrl = imageUrl
)
