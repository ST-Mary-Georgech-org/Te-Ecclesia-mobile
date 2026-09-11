package com.teEcclesia.identity.data.dataSource.remote.dto.auth.response

import com.teEcclesia.identity.domain.model.DeaconsSchoolRecord
import com.teEcclesia.identity.domain.model.DeaconsSchoolStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeaconsSchoolRecordDto(
    @SerialName("academicYear")
    val academicYear: Int,
    @SerialName("enrolled")
    val enrolled: Boolean,
    @SerialName("paid")
    val paid: Boolean,
    @SerialName("paidAmount")
    val paidAmount: Double,
    @SerialName("status")
    val status: DeaconsSchoolStatus
)

fun DeaconsSchoolRecordDto.toDomain(): DeaconsSchoolRecord {
    return DeaconsSchoolRecord(
        academicYear = academicYear,
        enrolled = enrolled,
        paid = paid,
        paidAmount = paidAmount,
        status = status
    )
}
