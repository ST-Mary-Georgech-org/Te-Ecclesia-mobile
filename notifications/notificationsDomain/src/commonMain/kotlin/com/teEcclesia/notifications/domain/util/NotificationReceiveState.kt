package com.teEcclesia.notifications.domain.util

import com.teEcclesia.notifications.domain.model.NotificationResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationReceiveState {
    private val _receiveFlow = MutableSharedFlow<NotificationResponse>(extraBufferCapacity = 64)
    val receiveFlow = _receiveFlow.asSharedFlow()

    fun onNotificationReceived(notification: NotificationResponse) {
        _receiveFlow.tryEmit(notification)
    }
}
