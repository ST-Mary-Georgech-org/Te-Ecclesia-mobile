package com.teEcclesia.notifications.data.push

import com.teEcclesia.shared.domain.push.NotificationPermissionHandler
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

class NotificationPermissionHandlerImpl : NotificationPermissionHandler {

    override fun checkPermission(onResult: (Boolean) -> Unit) {
        UNUserNotificationCenter.currentNotificationCenter().getNotificationSettingsWithCompletionHandler { settings ->
            val status = settings?.authorizationStatus
            val isGranted = status == UNAuthorizationStatusAuthorized || status == UNAuthorizationStatusProvisional
            onResult(isGranted)
        }
    }

    override fun openNotificationSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any?>(), completionHandler = null)
        }
    }
}
