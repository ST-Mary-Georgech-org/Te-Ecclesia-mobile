package com.teEcclesia.shared.domain.push

interface NotificationPermissionHandler {
    fun checkPermission(onResult: (Boolean) -> Unit)
    fun openNotificationSettings()
}
