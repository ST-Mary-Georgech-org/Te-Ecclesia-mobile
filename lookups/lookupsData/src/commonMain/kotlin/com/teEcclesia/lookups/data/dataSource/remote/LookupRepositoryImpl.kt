package com.teEcclesia.lookups.data.dataSource.remote

import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupRequest
import com.teEcclesia.lookups.data.dataSource.remote.dto.LookupResponseDto
import com.teEcclesia.lookups.data.dataSource.remote.dto.toDomain
import com.teEcclesia.lookups.domain.model.AreaResponse
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.lookups.domain.repository.LookupRepository
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

import com.teEcclesia.shared.domain.model.UserRole
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LookupRepositoryImpl(
    client: HttpClient
) : BaseRepository(client), LookupRepository {

    override suspend fun getRanks(pageQuery: PageQuery): PagedData<LookupResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/ranks") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun getEducationalStages(pageQuery: PageQuery, forRole: UserRole?): PagedData<LookupResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/educational-stages") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
                forRole?.let { parameter("forRole", it.name) }
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun getAreas(query: String?, pageQuery: PageQuery): PagedData<String> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/areas") {
                query?.takeIf { it.isNotBlank() }?.let { parameter("query", it) }
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.name }
    }

    override suspend fun getAreasForSuggestions(query: String?, pageQuery: PageQuery): PagedData<AreaResponse> {
        return tryToExecute<BasePagedData<LookupResponseDto>> {
            get("/api/v1/lookups/areas") {
                query?.takeIf { it.isNotBlank() }?.let { parameter("query", it) }
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { AreaResponse(it.id,it.name) }
    }

    override suspend fun createArea(area : AreaResponse) : LookupResponse{
        val dto = tryToExecute<LookupResponseDto> {
            post("/api/v1/lookups/areas") {
                contentType(ContentType.Application.Json)
                setBody(
                    LookupRequest(area.name)
                )
            }
        }
        return dto.toDomain()
    }

    override suspend fun updateArea(area: AreaResponse) : LookupResponse {
        val dto = tryToExecute<LookupResponseDto> {
            put("/api/v1/lookups/areas/${area.id}") {
                contentType(ContentType.Application.Json)
                setBody(
                    LookupRequest(area.name)
                )
            }
        }
        return dto.toDomain()
    }

    override suspend fun deleteArea(id : Long) {
        tryToExecute<Unit> {
            delete("/api/v1/lookups/areas/$id")
        }
    }
}
