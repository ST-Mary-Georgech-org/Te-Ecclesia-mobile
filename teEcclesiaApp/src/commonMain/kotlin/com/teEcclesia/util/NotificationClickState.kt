package com.teEcclesia.util

import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

object NotificationClickState {
    private var pendingPayload: PayloadData? = null

    private val _clickChannel = Channel<PayloadData>(Channel.BUFFERED)
    val clickFlow = _clickChannel.receiveAsFlow()

    fun onNotificationClicked(data: PayloadData) {
        if (data.isNotEmpty()) {
            _clickChannel.trySend(data)
        }
    }

    fun consumePendingPayload(): PayloadData? {
        val payload = pendingPayload
        pendingPayload = null
        return payload
    }
}
