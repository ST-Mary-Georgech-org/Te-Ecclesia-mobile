package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.MakhdoomProfileResponse
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MakhdoomProfileResponseDto(
    @SerialName("shamamsaStudyStatus")
    val shamamsaStudyStatus: ShamamsaStudyStatus,
    @SerialName("educationalStage")
    val educationalStage: LookupResponseDto,
    @SerialName("educationalYear")
    val educationalYear: LookupResponseDto? = null,
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

fun MakhdoomProfileResponseDto.toDomain() = MakhdoomProfileResponse(
    shamamsaStudyStatus = shamamsaStudyStatus,
    educationalStage = educationalStage.toDomain(),
    educationalYear = educationalYear?.toDomain(),
    fatherPhone = fatherPhone.orEmpty(),
    fatherWhatsapp = fatherWhatsapp.orEmpty(),
    motherPhone = motherPhone.orEmpty(),
    motherWhatsapp = motherWhatsapp.orEmpty(),
    isFatherDeceased = isFatherDeceased,
    isMotherDeceased = isMotherDeceased,
    identityDocumentImageUrl = identityDocumentImageUrl
)
