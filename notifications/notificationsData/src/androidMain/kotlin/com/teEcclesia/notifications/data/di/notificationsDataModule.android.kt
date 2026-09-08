package com.teEcclesia.notifications.data.di

import com.teEcclesia.notifications.data.push.NotificationPermissionHandlerImpl
import com.teEcclesia.notifications.data.scheduler.LocalNotificationSchedulerImpl
import com.teEcclesia.notifications.domain.scheduler.LocalNotificationScheduler
import com.teEcclesia.shared.domain.push.NotificationPermissionHandler
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformNotificationsDataModule: Module = module {
    single<LocalNotificationScheduler> { LocalNotificationSchedulerImpl() }
    single<NotificationPermissionHandler> { NotificationPermissionHandlerImpl(get()) }
}

