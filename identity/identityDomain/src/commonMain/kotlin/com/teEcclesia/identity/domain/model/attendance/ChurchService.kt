package com.teEcclesia.identity.domain.model.attendance

import kotlinx.datetime.LocalDateTime

data class ChurchService(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime
)
