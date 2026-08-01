package com.teEcclesia.notifications.domain.scheduler

import com.teEcclesia.notifications.domain.model.NotificationType

interface LocalNotificationScheduler {
    fun schedule(
        id: Int,
        title: String,
        body: String,
        delayMs: Long,
        notificationType: NotificationType
    )
    fun cancelAll(startId: Int, endId: Int)
}
