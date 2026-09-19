package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.ForgotPasswordResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponseDto(
    @SerialName("link")
    val link: String? = null,
    @SerialName("token")
    val token: String? = null,
    @SerialName("deletedAccount")
    val isDeletedAccount: Boolean? = false
)

fun ForgotPasswordResponseDto.toDomain() = ForgotPasswordResponse(
    link = link,
    token = token,
    isDeletedAccount = isDeletedAccount ?: false
)

