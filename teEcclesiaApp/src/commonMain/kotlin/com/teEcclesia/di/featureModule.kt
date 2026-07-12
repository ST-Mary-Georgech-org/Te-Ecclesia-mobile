package com.teEcclesia.di

import com.teEcclesia.identity.domain.di.domainModule
import com.teEcclesia.identity.presentation.di.identityScreensModule
import com.teEcclesia.identity.data.di.identityDataModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        identityScreensModule,
        domainModule,
        identityDataModule,
        )
}
