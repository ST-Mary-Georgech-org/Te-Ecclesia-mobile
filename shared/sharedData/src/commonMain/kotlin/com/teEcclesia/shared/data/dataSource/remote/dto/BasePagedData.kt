package com.teEcclesia.shared.data.dataSource.remote.dto

import com.teEcclesia.shared.domain.utils.PagedData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePagedData<T>(
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("totalElements")
    val totalElements: Long?,
    @SerialName("first")
    val first: Boolean,
    @SerialName("last")
    val last: Boolean,
    @SerialName("size")
    val size: Int,
    @SerialName("content")
    val content: List<T>?,
    @SerialName("number")
    val number: Int,
    @SerialName("sort")
    val sort: SortDto,
    @SerialName("numberOfElements")
    val numberOfElements: Int,
    @SerialName("pageable")
    val pageable: PageableMetaDto,
    @SerialName("empty")
    val empty: Boolean,
)

fun <DTO, ENTITY> BasePagedData<DTO>.toPagedData(mapper: (DTO) -> ENTITY?): PagedData<ENTITY> {
    return PagedData(
        data = content?.mapNotNull(mapper) ?: emptyList(),
        totalItems = totalElements ?: 0L,
        isLastPage = last,
    )
}
