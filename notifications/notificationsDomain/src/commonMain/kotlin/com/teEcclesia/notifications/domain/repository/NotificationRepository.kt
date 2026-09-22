package com.teEcclesia.notifications.domain.repository

import com.teEcclesia.notifications.domain.model.AdminSendNotificationParam
import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.shared.domain.utils.PagedData
import com.teEcclesia.shared.domain.utils.PageQuery

interface NotificationRepository {
    suspend fun getAllNotifications(pageQuery: PageQuery): PagedData<NotificationResponse>
    suspend fun getUnreadCount(): Long
    suspend fun markAllAsRead()
    suspend fun sendAdminNotification(param: AdminSendNotificationParam)
    suspend fun deleteNotification(id: String)
}

