package com.teEcclesia.identity.data.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.tencent.mmkv.MMKV
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformIdentityDataModule: Module = module {
    single<Settings> {
        val context = get<Context>()
        val defaultSpName = "${context.packageName}_preferences"
        val oldPreferences = context.getSharedPreferences(defaultSpName, Context.MODE_PRIVATE)
        val mmkv = MMKV.defaultMMKV()
        if (oldPreferences.all.isNotEmpty()) {
            mmkv.importFromSharedPreferences(oldPreferences)
            oldPreferences.edit().clear().apply()
        }
        SharedPreferencesSettings(mmkv)
    }

    single<AppLocalizer>(createdAtStart = true) {
        AppLocalizer(
            context = get(),
            settingsRepository = get()
        )
    }
}
