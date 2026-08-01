package com.teEcclesia.notifications.presentation.di

import com.teEcclesia.notifications.api.NotificationsFeatureApi
import com.teEcclesia.notifications.presentation.api.NotificationsFeatureApiImpl
import com.teEcclesia.notifications.presentation.screen.NotificationsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val notificationsPresentationModule = module {
    singleOf(::NotificationsFeatureApiImpl) bind NotificationsFeatureApi::class
    viewModelOf(::NotificationsViewModel)
}
