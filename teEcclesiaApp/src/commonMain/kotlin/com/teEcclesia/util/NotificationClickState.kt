package com.teEcclesia.util

import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationClickState {
    private var pendingPayload: PayloadData? = null
    private val _clickFlow = MutableSharedFlow<PayloadData>(extraBufferCapacity = 1)
    val clickFlow = _clickFlow.asSharedFlow()

    fun onNotificationClicked(data: PayloadData) {
        pendingPayload = data
        _clickFlow.tryEmit(data)
    }

    fun consumePendingPayload(): PayloadData? {
        val payload = pendingPayload
        pendingPayload = null
        return payload
    }
}
