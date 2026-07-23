package com.teEcclesia.identity.domain.model

data class KhademProfileRequest(
    val educationalStageId: Long,
    val educationalYearId: Long? = null
)
