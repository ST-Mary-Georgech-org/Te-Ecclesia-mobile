package com.teEcclesia.notifications.domain.repository

import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.model.UnreadCountResponse
import com.teEcclesia.shared.domain.utils.PagedData
import com.teEcclesia.shared.domain.utils.PageQuery

interface NotificationRepository {
    suspend fun getAllNotifications(pageQuery: PageQuery): PagedData<NotificationResponse>
    suspend fun getUnreadCount(): UnreadCountResponse
    suspend fun markAllAsRead()
}
