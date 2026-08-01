package com.teEcclesia.identity.domain.model

data class KhademProfileRequest(
    val educationalStageId: Long,
    val educationalYearId: Long? = null,
    val canApproveRequests: Boolean = false,
    val responsibleStageIds: List<Long> = emptyList(),
    val responsibleYearIds: List<Long> = emptyList()
)
