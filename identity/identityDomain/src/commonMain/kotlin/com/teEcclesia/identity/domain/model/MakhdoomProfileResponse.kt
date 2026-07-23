package com.teEcclesia.identity.domain.model

import com.teEcclesia.lookups.domain.model.LookupResponse

data class MakhdoomProfileResponse(
    val shamamsaStudyStatus: ShamamsaStudyStatus,
    val educationalStage: LookupResponse,
    val educationalYear: LookupResponse?,
    val fatherPhone: String,
    val fatherWhatsapp: String,
    val motherPhone: String,
    val motherWhatsapp: String,
    val isFatherDeceased: Boolean = false,
    val isMotherDeceased: Boolean = false,
    val identityDocumentImageUrl: String?
)
