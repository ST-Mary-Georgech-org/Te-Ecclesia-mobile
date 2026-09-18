package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.AccountDeletionRequest
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDeletionRequestResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("userId")
    val userId: String,
    @SerialName("userName")
    val userName: String,
    @SerialName("userCode")
    val userCode: String? = null,
    @SerialName("userImageUrl")
    val userImageUrl: String? = null,
    @SerialName("userRole")
    val userRole: UserRole,
    @SerialName("reason")
    val reason: String,
    @SerialName("requestedAt")
    val requestedAt: String
)

fun AccountDeletionRequestResponseDto.toDomain() = AccountDeletionRequest(
    id = id,
    userId = userId,
    userName = userName,
    userCode = userCode,
    userImageUrl = userImageUrl,
    userRole = userRole,
    reason = reason,
    requestedAt = requestedAt.toLocalDateTimeOrDefault()
)
