package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.domain.model.VerificationMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequestDto(
    @SerialName("key")
    val key: String,
    @SerialName("method")
    val method: VerificationMethod
)
