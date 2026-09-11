package com.teEcclesia.identity.domain.model

data class DeaconsSchoolRecord(
    val academicYear: Int,
    val enrolled: Boolean,
    val paid: Boolean,
    val paidAmount: Double,
    val status: DeaconsSchoolStatus
)
