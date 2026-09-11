package com.teEcclesia.util

import com.mmk.kmpnotifier.notification.PayloadData
import com.teEcclesia.AppEnvironment
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.identity.api.ReviewAndEditRequestRoute
import com.teEcclesia.notifications.api.NotificationsRoute
import com.teEcclesia.notifications.domain.model.NotificationType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun handleNotificationClick(
    data: PayloadData,
    coroutineScope: CoroutineScope,
    exceptionHandler: CoroutineExceptionHandler,
    effector: Effector,
    openLink: ((String) -> Unit)? = null
) {
    val rawUrl = (data["url"] ?: data["link"] ?: data["uri"]) as? String
    val url = rawUrl?.trim()?.takeIf { it.isNotBlank() }

    val typeStr = data["type"] as? String
    val type = typeStr?.let { NotificationType.fromStringOrDefault(it) }

    coroutineScope.launch(exceptionHandler) {
        when {
            type == NotificationType.REVIEW -> {
                val id = (data["id"] ?: data["request_id"] ?: data["requestId"]) as? String
                if (id != null) {
                    effector.resetTo(
                        listOf(
                            RegistrationRequestsRoute,
                            ReviewAndEditRequestRoute(id)
                        ),
                        forceNavigate = true
                    )
                } else {
                    effector.resetTo(RegistrationRequestsRoute, forceNavigate = true)
                }
            }

            url != null && openLink != null -> {
                val resolvedUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                    url
                } else {
                    val baseUrl = AppEnvironment.baseUrl.removeSuffix("/")
                    val path = if (url.startsWith("/")) url else "/$url"
                    "$baseUrl$path"
                }
                openLink(resolvedUrl)
            }

            else -> {
                effector.resetTo(NotificationsRoute, forceNavigate = true)
            }
        }
    }
}
