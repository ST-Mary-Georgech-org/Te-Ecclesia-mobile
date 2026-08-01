package com.teEcclesia.notifications.data.di

import com.teEcclesia.notifications.data.dataSource.remote.NotificationRespositoryImpl
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformNotificationsDataModule: Module

val notificationsDataModule = module {
    includes(platformNotificationsDataModule)
    singleOf(::NotificationRespositoryImpl) bind NotificationRepository::class
}
