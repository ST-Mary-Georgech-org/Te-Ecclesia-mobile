package com.teEcclesia.lookups.domain.repository

import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.utils.PagedData
import com.teEcclesia.shared.domain.utils.PageQuery

interface LookupRepository {
    suspend fun getRanks(pageQuery: PageQuery): PagedData<LookupResponse>
    suspend fun getEducationalStages(pageQuery: PageQuery): PagedData<LookupResponse>
    suspend fun getAreas(pageQuery: PageQuery): PagedData<LookupResponse>
}
