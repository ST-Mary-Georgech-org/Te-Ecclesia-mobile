package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.Gender
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("secondName")
    val secondName: String,
    @SerialName("thirdName")
    val thirdName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("email")
    val email: String?,
    @SerialName("isEmailVerified")
    val isEmailVerified: Boolean,
    @SerialName("isPhoneVerified")
    val isPhoneVerified: Boolean,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("specialMark")
    val specialMark: String,
    @SerialName("gender")
    val gender: Gender,
    @SerialName("status")
    val status: UserStatus,
    @SerialName("statusReason")
    val statusReason: String?,
    @SerialName("role")
    val role: UserRole,
    @SerialName("khademProfile")
    val khademProfile: KhademProfileResponseDto? = null,
    @SerialName("parentProfile")
    val parentProfile: ParentProfileResponseDto? = null
)

fun ProfileResponseDto.toDomain() = ProfileResponse(
    id = id,
    firstName = firstName,
    secondName = secondName,
    thirdName = thirdName,
    lastName = lastName,
    displayName = displayName,
    fullName = fullName,
    phone = phone,
    email = email,
    isEmailVerified = isEmailVerified,
    isPhoneVerified = isPhoneVerified,
    imageUrl = imageUrl,
    specialMark = specialMark,
    gender = gender,
    status = status,
    statusReason = statusReason,
    role = role,
    khademProfile = khademProfile?.toDomain(),
    parentProfile = parentProfile?.toDomain()
)
