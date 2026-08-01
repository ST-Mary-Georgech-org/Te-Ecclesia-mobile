package com.teEcclesia.notifications.data.scheduler

import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import com.teEcclesia.notifications.domain.model.NotificationType
import com.teEcclesia.notifications.domain.scheduler.LocalNotificationScheduler
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class LocalNotificationSchedulerImpl : LocalNotificationScheduler {

    @OptIn(ExperimentalTime::class)
    override fun schedule(
        id: Int,
        title: String,
        body: String,
        delayMs: Long,
        notificationType: NotificationType
    ) {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        KMPNotifier.localNotifier.notify {
            this.id = id
            this.title = title
            this.body = body
            this.payloadData = mapOf("type" to notificationType.name)
            this.scheduledAt = nowMs + delayMs
        }
    }

    override fun cancelAll(startId: Int, endId: Int) {
        for (id in startId..endId) {
            KMPNotifier.localNotifier.remove(id)
        }
    }
}
