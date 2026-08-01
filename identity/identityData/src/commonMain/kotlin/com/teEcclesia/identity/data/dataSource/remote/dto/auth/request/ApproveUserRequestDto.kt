package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.domain.model.ApproveUserRequest
import kotlinx.serialization.Serializable

@Serializable
data class ApproveUserRequestDto(
    val customCode: String? = null,
    val updateProfileData: RegisterRequestDto? = null
)

fun ApproveUserRequest.toDto(): ApproveUserRequestDto = ApproveUserRequestDto(
    customCode = customCode,
    updateProfileData = updateProfileData?.toDto()
)
