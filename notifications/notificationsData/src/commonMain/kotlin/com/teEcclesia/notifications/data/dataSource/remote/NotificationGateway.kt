package com.teEcclesia.notifications.data.dataSource.remote

import com.teEcclesia.notifications.data.dataSource.remote.dto.NotificationResponseDto
import com.teEcclesia.notifications.data.dataSource.remote.dto.UnreadCountResponseDto
import com.teEcclesia.notifications.data.dataSource.remote.dto.toDomain
import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.model.UnreadCountResponse
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.data.shared.BaseGateway
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.parameter

class NotificationGateway(
    client: HttpClient
) : BaseGateway(client), NotificationRepository {

    override suspend fun getAllNotifications(pageQuery: PageQuery): PagedData<NotificationResponse> {
        return tryToExecute<BasePagedData<NotificationResponseDto>> {
            get("/api/v1/notifications") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.body<BasePagedData<NotificationResponseDto>>().toPagedData { it.toDomain() }
    }

    override suspend fun getUnreadCount(): UnreadCountResponse {
        return tryToExecute<UnreadCountResponseDto> {
            get("/api/v1/notifications/unread-count")
        }.body<UnreadCountResponseDto>().toDomain()
    }

    override suspend fun markAllAsRead() {
        tryToExecute<Unit> {
            patch("/api/v1/notifications/mark-all-read")
        }
    }
}
