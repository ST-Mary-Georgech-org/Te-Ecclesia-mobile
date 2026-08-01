package com.teEcclesia.notifications.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import com.teEcclesia.notifications.domain.model.NotificationType
import com.teEcclesia.notifications.domain.scheduler.LocalNotificationScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocalNotificationSchedulerImpl : LocalNotificationScheduler, KoinComponent {

    private val context: Context by inject()

    override fun schedule(
        id: Int,
        title: String,
        body: String,
        delayMs: Long,
        notificationType: NotificationType
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderDelayReceiver::class.java).apply {
            putExtra("id", id)
            putExtra("title", title)
            putExtra("body", body)
            putExtra("type", notificationType.name)
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags)
        val triggerAtMillis = System.currentTimeMillis() + delayMs

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    override fun cancelAll(startId: Int, endId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val flags = PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE

        for (id in startId..endId) {
            val intent = Intent(context, ReminderDelayReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, flags)
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
            KMPNotifier.localNotifier.remove(id)
        }
    }
}
