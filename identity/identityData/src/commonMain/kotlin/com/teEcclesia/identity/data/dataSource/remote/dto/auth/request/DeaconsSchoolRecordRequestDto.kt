package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.domain.model.DeaconsSchoolRecordRequest
import com.teEcclesia.identity.domain.model.DeaconsSchoolStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeaconsSchoolRecordRequestDto(
    @SerialName("enrolled")
    val enrolled: Boolean,
    @SerialName("paid")
    val paid: Boolean,
    @SerialName("paidAmount")
    val paidAmount: Double,
    @SerialName("status")
    val status: DeaconsSchoolStatus
)

fun DeaconsSchoolRecordRequest.toDto(): DeaconsSchoolRecordRequestDto = DeaconsSchoolRecordRequestDto(
    enrolled = enrolled,
    paid = paid,
    paidAmount = paidAmount,
    status = status
)
