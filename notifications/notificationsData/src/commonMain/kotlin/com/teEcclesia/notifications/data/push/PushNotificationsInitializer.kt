package com.teEcclesia.notifications.data.push

import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.PayloadData
import com.mmk.kmpnotifier.push.PushListener
import com.mmk.kmpnotifier.push.firebase.addPushListener
import com.mmk.kmpnotifier.push.firebase.firebasePushNotifier
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.notifications.data.util.NotificationClickState
import com.teEcclesia.notifications.domain.model.NotificationResponse
import com.teEcclesia.notifications.domain.model.NotificationType
import com.teEcclesia.notifications.domain.util.NotificationReceiveState
import com.teEcclesia.shared.domain.push.PushTokenProvider
import com.teEcclesia.shared.domain.utils.toLocalDateTimeOrDefault
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import com.teEcclesia.designsystem.navigation.SnackBarManager
import com.teEcclesia.designsystem.utils.UiText
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.push_token_connection_failed
import teecclesia.designsystem.generated.resources.push_token_connection_failed_message
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

object PushNotificationsInitializer : PushTokenProvider {

    private var isListening = false

    fun initialize(showPushNotification: Boolean = true) {
        onPlatformPushNotificationInitialization(showPushNotification)

        KMPNotifier.addListener(object : KMPNotifier.Listener {
            override fun onNotificationClicked(data: PayloadData) {
                NotificationClickState.onNotificationClicked(data)
            }
        })
    }

    fun listenToNotifications(
        coroutineScope: CoroutineScope,
        exceptionHandler: CoroutineExceptionHandler,
    ) {
        if (isListening) return
        isListening = true

        KMPNotifier.addPushListener(object : PushListener {
            override fun onNewToken(token: String) {
                val authenticationRepository = KoinPlatform.getKoin().getOrNull<AuthenticationRepository>()

                coroutineScope.launch(exceptionHandler) {
                    try {
                        authenticationRepository?.updateDeviceToken(token)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onPushNotificationWithPayloadData(
                title: String?,
                body: String?,
                data: PayloadData
            ) {
                super.onPushNotificationWithPayloadData(title, body, data)
                val notification = data.toNotificationResponse(title, body)
                NotificationReceiveState.onNotificationReceived(notification)
            }
        })
    }

    override suspend fun getToken(): String? {
        var token: String? = null
        repeat(3) { attempt ->
            token = runCatching { KMPNotifier.firebasePushNotifier.getToken() }.getOrNull()
            if (!token.isNullOrBlank()) return@repeat
            if (attempt < 2) delay(500.milliseconds)
        }
        if (token.isNullOrBlank()) {
            try {
                val snackBarManager = KoinPlatform.getKoin().getOrNull<SnackBarManager>()
                snackBarManager?.showSnackBar(
                    title = UiText.StringRes(Res.string.push_token_connection_failed),
                    message = UiText.StringRes(Res.string.push_token_connection_failed_message),
                    isSuccess = false,
                    duration = 3000L
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return token
    }

    suspend fun subscribeToTopic(topic: String) {
        KMPNotifier.firebasePushNotifier.subscribeToTopic(topic)
    }

    suspend fun unsubscribeFromTopic(topic: String) {
        KMPNotifier.firebasePushNotifier.unSubscribeFromTopic(topic)
    }

    override suspend fun deleteToken() {
        KMPNotifier.firebasePushNotifier.deleteMyToken()
    }

    @OptIn(ExperimentalTime::class)
    private fun PayloadData.toNotificationResponse(
        title: String? = null,
        body: String? = null
    ): NotificationResponse {
        val rawId = this["id"]?.toString()
            ?: this["notification_id"]?.toString()
            ?: "notif_${Clock.System.now().toEpochMilliseconds()}"

        val rawTitle = title
            ?: this["title"]?.toString()
            ?: ""

        val rawBody = body
            ?: this["body"]?.toString()
            ?: this["message"]?.toString()
            ?: ""

        val rawType = NotificationType.fromStringOrDefault(this["type"]?.toString())

        val rawSentAt = (this["sent_at"]?.toString() ?: this["sentAt"]?.toString() ?: this["created_at"]?.toString())
            ?.toLocalDateTimeOrDefault()
            ?: getNow()

        val rawIsRead = this["is_read"]?.toString()?.lowercase() == "true" || this["read"]?.toString()?.lowercase() == "true"

        return NotificationResponse(
            id = rawId,
            title = rawTitle,
            message = rawBody,
            type = rawType,
            sentAt = rawSentAt,
            isRead = rawIsRead
        )
    }
}
