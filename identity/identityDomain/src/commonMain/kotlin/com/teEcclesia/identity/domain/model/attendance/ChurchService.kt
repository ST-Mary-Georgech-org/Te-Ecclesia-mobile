package com.teEcclesia.identity.domain.model.attendance

import com.teEcclesia.lookups.domain.model.LookupResponse
import kotlinx.datetime.LocalDateTime

data class ChurchService(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
    val isResponsible: Boolean,
    val educationalStages: List<LookupResponse>,
    val responsibleServants: List<ResponsibleServant>
)
