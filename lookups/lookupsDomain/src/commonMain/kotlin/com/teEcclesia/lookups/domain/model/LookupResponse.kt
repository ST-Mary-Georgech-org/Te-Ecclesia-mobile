package com.teEcclesia.lookups.domain.model

data class LookupResponse(
    val id: Long,
    val name: String,
    val subItems: List<LookupResponse>,
    val whatsAppLink: String? = null,
    val isKhademOnly: Boolean = false
)
