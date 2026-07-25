package com.teEcclesia.identity.domain.model

data class Priest(
    val id: String,
    val name: String,
    val ordinationDate: String? = null
)
