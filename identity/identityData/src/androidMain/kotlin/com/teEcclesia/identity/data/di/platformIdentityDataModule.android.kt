package com.teEcclesia.identity.data.di

import com.teEcclesia.identity.domain.util.AppLocalizer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformIdentityDataModule: Module = module {
    single<AppLocalizer>(createdAtStart = true) {
        AppLocalizer(
            context = get(),
            settingsRepository = get()
        )
    }
}
