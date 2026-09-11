package com.teEcclesia.identity.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateServiceDto(
    @SerialName("name")
    val name: String,
    @SerialName("educationalStageId")
    val educationalStageId: Long?,
    @SerialName("responsibleServantIds")
    val responsibleServantIds: List<String>
)
