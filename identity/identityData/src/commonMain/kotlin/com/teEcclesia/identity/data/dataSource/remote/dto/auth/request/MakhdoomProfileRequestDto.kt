package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.teEcclesia.identity.domain.model.MakhdoomProfileRequest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus

@Serializable
data class MakhdoomProfileRequestDto(
    @SerialName("shamamsaStudyStatus")
    val shamamsaStudyStatus: ShamamsaStudyStatus,
    @SerialName("educationalStageId")
    val educationalStageId: Long,
    @SerialName("educationalYearId")
    val educationalYearId: Long? = null,
    @SerialName("fatherPhone")
    val fatherPhone: String? = null,
    @SerialName("fatherWhatsapp")
    val fatherWhatsapp: String? = null,
    @SerialName("motherPhone")
    val motherPhone: String? = null,
    @SerialName("motherWhatsapp")
    val motherWhatsapp: String? = null,
    @SerialName("isFatherDeceased")
    val isFatherDeceased: Boolean = false,
    @SerialName("isMotherDeceased")
    val isMotherDeceased: Boolean = false,
    @SerialName("identityDocumentImageUrl")
    val identityDocumentImageUrl: String? = null
)

fun MakhdoomProfileRequest.toDto() = MakhdoomProfileRequestDto(
    shamamsaStudyStatus = shamamsaStudyStatus,
    educationalStageId = educationalStageId,
    educationalYearId = educationalYearId,
    fatherPhone = fatherPhone,
    fatherWhatsapp = fatherWhatsapp,
    motherPhone = motherPhone,
    motherWhatsapp = motherWhatsapp,
    isFatherDeceased = isFatherDeceased,
    isMotherDeceased = isMotherDeceased,
    identityDocumentImageUrl = identityDocumentImageUrl
)
