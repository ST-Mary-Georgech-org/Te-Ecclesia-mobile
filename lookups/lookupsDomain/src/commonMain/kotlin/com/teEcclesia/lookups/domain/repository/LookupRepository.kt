package com.teEcclesia.lookups.domain.repository

import com.teEcclesia.lookups.domain.model.AreaResponse
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.PagedData
import com.teEcclesia.shared.domain.utils.PageQuery

interface LookupRepository {
    suspend fun getRanks(pageQuery: PageQuery): PagedData<LookupResponse>
    suspend fun getEducationalStages(pageQuery: PageQuery, forRole: UserRole? = null): PagedData<LookupResponse>
    suspend fun getAreas(query: String? = null, pageQuery: PageQuery): PagedData<String>
    suspend fun getAreasForSuggestions(query: String?, pageQuery: PageQuery): PagedData<AreaResponse>
    suspend fun createArea(area : AreaResponse): LookupResponse
    suspend fun updateArea(area : AreaResponse): LookupResponse
    suspend fun deleteArea(id : Long)
}
