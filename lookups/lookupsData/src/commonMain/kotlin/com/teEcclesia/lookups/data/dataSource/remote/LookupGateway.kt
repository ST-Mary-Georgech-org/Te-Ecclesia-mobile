package com.teEcclesia.lookups.data.dataSource.remote

import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.data.shared.BaseGateway
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LookupGateway(
    client: HttpClient
) : BaseGateway(client), LookupRepository {

    override suspend fun getRanks(pageQuery: PageQuery): PagedData<LookupResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/ranks") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun getEducationalStages(pageQuery: PageQuery): PagedData<LookupResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/educational-stages") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun getAreas(pageQuery: PageQuery): PagedData<LookupResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/areas") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }
}
