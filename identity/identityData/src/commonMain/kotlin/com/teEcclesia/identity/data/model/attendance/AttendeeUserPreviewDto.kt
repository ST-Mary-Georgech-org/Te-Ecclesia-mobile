package com.teEcclesia.identity.data.model.attendance

import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendeeUserPreviewDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("role")
    val role: String,
    @SerialName("stageName")
    val stageName: String? = null,
    @SerialName("yearName")
    val yearName: String? = null,
    @SerialName("code")
    val code: String? = null
)

fun AttendeeUserPreviewDto.toDomain(): AttendeeUserPreview {
    return AttendeeUserPreview(
        id = id,
        name = name,
        role = runCatching { UserRole.valueOf(role) }.getOrDefault(UserRole.MAKHDOOM),
        stageName = stageName,
        yearName = yearName,
        code = code
    )
}
