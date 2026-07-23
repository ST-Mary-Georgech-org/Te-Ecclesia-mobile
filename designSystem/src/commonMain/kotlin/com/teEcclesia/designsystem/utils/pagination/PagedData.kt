package com.teEcclesia.designsystem.utils.pagination

data class PagedData<T>(
    val data: List<T>,
    val totalItems: Long,
    val isLastPage: Boolean,
)
