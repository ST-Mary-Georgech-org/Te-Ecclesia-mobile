package com.teEcclesia.identity.data.dataSource.remote.dto.auth.request

import com.teEcclesia.identity.domain.model.ApproveUserRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApproveUserRequestDto(
    @SerialName("customCode") val customCode: String? = null,
    @SerialName("updateProfileData") val updateProfileData: RegisterRequestDto? = null,
    @SerialName("deaconsSchoolRecord") val deaconsSchoolRecord: DeaconsSchoolRecordRequestDto? = null
)

fun ApproveUserRequest.toDto(): ApproveUserRequestDto = ApproveUserRequestDto(
    customCode = customCode,
    updateProfileData = updateProfileData?.toDto(),
    deaconsSchoolRecord = deaconsSchoolRecord?.toDto()
)
