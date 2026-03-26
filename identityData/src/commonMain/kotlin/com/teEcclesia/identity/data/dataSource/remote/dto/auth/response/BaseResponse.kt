package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response


import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int? = null,
    val message: String? = null,
    val data: T? = null
)