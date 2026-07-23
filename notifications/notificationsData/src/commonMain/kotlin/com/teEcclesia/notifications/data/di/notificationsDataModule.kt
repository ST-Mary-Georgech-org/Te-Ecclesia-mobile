package com.teEcclesia.notifications.data.di

import com.teEcclesia.notifications.data.dataSource.remote.NotificationRespositoryImpl
import com.teEcclesia.notifications.domain.repository.NotificationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notificationsDataModule = module {
    singleOf(::NotificationRespositoryImpl) bind NotificationRepository::class
}
