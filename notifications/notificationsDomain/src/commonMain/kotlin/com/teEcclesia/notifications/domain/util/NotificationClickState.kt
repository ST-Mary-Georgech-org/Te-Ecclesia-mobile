package com.teEcclesia.notifications.domain.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationClickState {
    private var pendingPayload: Map<String, Any?>? = null

    private val _clickFlow = MutableSharedFlow<Map<String, Any?>>(extraBufferCapacity = 1)
    val clickFlow = _clickFlow.asSharedFlow()

    fun onNotificationClicked(data: Map<String, Any?>) {
        pendingPayload = data
        _clickFlow.tryEmit(data)
    }

    fun consumePendingPayload(): Map<String, Any?>? {
        val payload = pendingPayload
        pendingPayload = null
        return payload
    }
}
