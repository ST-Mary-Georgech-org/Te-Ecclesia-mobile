package com.teEcclesia.identity.presentation.util

import com.teEcclesia.designsystem.utils.pagination.PagedData as DSPagedData
import com.teEcclesia.shared.domain.utils.PagedData as DomainPagedData

fun <T> DomainPagedData<T>.toPagedData(): DSPagedData<T> {
    return DSPagedData(
        data = this.data,
        totalItems = this.totalItems,
        isLastPage = this.isLastPage
    )
}
