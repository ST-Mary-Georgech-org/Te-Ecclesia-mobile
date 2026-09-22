package com.teEcclesia.notifications.data.dataSource.remote

import com.teEcclesia.notifications.data.dataSource.remote.dto.NotificationResponseDto
import com.teEcclesia.notifications.data.dataSource.remote.dto.UnreadCountResponseDto
import com.teEcclesia.notifications.data.dataSource.remote.dto.toDomain
import com.teEcclesia.notifications.data.dataSource.remote.dto.toDto
import com.teEcclesia.notifications.domain.model.AdminSendNotificationParam
import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import com.teEcclesia.shared.data.dataSource.remote.dto.BasePagedData
import com.teEcclesia.shared.data.dataSource.remote.dto.toPagedData
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.shared.domain.utils.PageQuery
import com.teEcclesia.shared.domain.utils.PagedData
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class NotificationRespositoryImpl(
    client: HttpClient
) : BaseRepository(client), NotificationRepository {

    override suspend fun getAllNotifications(pageQuery: PageQuery): PagedData<NotificationResponse> {
        return tryToExecute<BasePagedData<NotificationResponseDto>> {
            get("/api/v1/notifications") {
                parameter("page", pageQuery.page)
                parameter("size", pageQuery.size)
            }
        }.toPagedData { it.toDomain() }
    }

    override suspend fun getUnreadCount(): Long {
        return tryToExecute<UnreadCountResponseDto> {
            get("/api/v1/notifications/unread-count")
        }.unreadCount
    }

    override suspend fun markAllAsRead() {
        tryToExecute<Unit> {
            patch("/api/v1/notifications/mark-all-read")
        }
    }

    override suspend fun sendAdminNotification(param: AdminSendNotificationParam) {
        tryToExecute<Unit> {
            post("/api/v1/admin/notifications/send") {
                setBody(param.toDto())
            }
        }
    }

    override suspend fun deleteNotification(id: String) {
        tryToExecute<Unit> {
            delete("/api/v1/notifications/$id")
        }
    }
}
