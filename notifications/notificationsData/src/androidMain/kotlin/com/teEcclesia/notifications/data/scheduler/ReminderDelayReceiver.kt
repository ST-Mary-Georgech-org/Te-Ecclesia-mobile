package com.teEcclesia.notifications.data.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import com.teEcclesia.notifications.domain.model.NotificationType

class ReminderDelayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra("id", 1000) ?: 1000
        val title = intent?.getStringExtra("title") ?: "تذكره جديدة"
        val body = intent?.getStringExtra("body") ?: "لديك تذكره جديدة"
        val type = intent?.getStringExtra("type") ?: NotificationType.SYSTEM.name

        KMPNotifier.localNotifier.notify {
            this.id = id
            this.title = title
            this.body = body
            this.payloadData = mapOf("type" to type)
        }
    }
}
