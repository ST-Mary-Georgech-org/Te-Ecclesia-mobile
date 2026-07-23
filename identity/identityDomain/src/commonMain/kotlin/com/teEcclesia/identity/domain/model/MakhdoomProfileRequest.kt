package com.teEcclesia.identity.domain.model

data class MakhdoomProfileRequest(
    val shamamsaStudyStatus: ShamamsaStudyStatus,
    val educationalStageId: Long,
    val educationalYearId: Long? = null,
    val fatherPhone: String? = null,
    val fatherWhatsapp: String? = null,
    val motherPhone: String? = null,
    val motherWhatsapp: String? = null,
    val isFatherDeceased: Boolean = false,
    val isMotherDeceased: Boolean = false,
    val identityDocumentImageUrl: String? = null
)
