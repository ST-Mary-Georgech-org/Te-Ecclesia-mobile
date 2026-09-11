package com.teEcclesia.identity.domain.model

data class ApproveUserRequest(
    val customCode: String?,
    val updateProfileData: RegisterRequest?,
    val deaconsSchoolRecord: DeaconsSchoolRecordRequest?
)
