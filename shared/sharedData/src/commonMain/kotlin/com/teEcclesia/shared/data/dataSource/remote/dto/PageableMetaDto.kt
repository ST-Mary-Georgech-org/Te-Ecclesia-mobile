package com.teEcclesia.shared.data.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageableMetaDto(
    @SerialName("offset")
    val offset: Long,
    @SerialName("sort")
    val sort: SortDto,
    @SerialName("unpaged")
    val unpaged: Boolean,
    @SerialName("paged")
    val paged: Boolean,
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("pageSize")
    val pageSize: Int,
)
