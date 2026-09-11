package com.teEcclesia.identity.data.model.attendance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateServiceDto(
    @SerialName("name")
    val name: String,
    @SerialName("educationalStageIds")
    val educationalStageIds: List<Long>,
    @SerialName("responsibleServantIds")
    val responsibleServantIds: List<String>
)
