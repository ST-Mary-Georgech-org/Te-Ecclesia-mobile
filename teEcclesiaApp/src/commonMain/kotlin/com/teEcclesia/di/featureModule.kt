package com.teEcclesia.di

import com.teEcclesia.identity.domain.di.domainModule
import com.teEcclesia.identity.presentation.di.identityScreensModule
import com.teEcclesia.identity.data.di.identityDataModule
import com.teEcclesia.lookups.data.di.lookupsDataModule
import com.teEcclesia.notifications.data.di.notificationsDataModule
import com.teEcclesia.notifications.presentation.di.notificationsPresentationModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        identityScreensModule,
        domainModule,
        identityDataModule,
        lookupsDataModule,
        notificationsDataModule,
        notificationsPresentationModule,
    )
}
