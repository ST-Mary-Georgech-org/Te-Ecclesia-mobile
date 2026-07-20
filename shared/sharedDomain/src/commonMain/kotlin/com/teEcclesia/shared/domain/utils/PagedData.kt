package com.teEcclesia.shared.domain.utils

data class PagedData<T>(
    val data: List<T>,
    val totalItems: Long,
    val isLastPage: Boolean,
) {
    companion object {
        fun <T> empty(): PagedData<T> = PagedData(emptyList(), 0, true)
    }
}

fun <T> PagedData<T>?.orEmpty(): PagedData<T> = this ?: PagedData.empty()

data class PageQuery(
    val page: Int,
    val size: Int,
    val sort: List<String>? = null,
)
