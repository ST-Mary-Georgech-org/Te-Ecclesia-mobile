package com.teEcclesia.identity.data.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.tencent.mmkv.MMKV
import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.core.content.edit
import com.teEcclesia.logging.CrashLogger

actual val platformIdentityDataModule: Module = module {
    single<Settings> {
        val context = get<Context>()
        val logger = get<CrashLogger>()
        val defaultSpName = "${context.packageName}_preferences"
        val oldPreferences = context.getSharedPreferences(defaultSpName, Context.MODE_PRIVATE)
        try {
            val mmkv = MMKV.defaultMMKV()
            if (oldPreferences.all.isNotEmpty()) {
                mmkv.importFromSharedPreferences(oldPreferences)
                oldPreferences.edit { clear() }
            }
            SharedPreferencesSettings(mmkv)
        } catch (t: Throwable) {
            android.util.Log.e("platformIdentityDataModule", "MMKV initialization failed, falling back to SharedPreferences", t)
            logger.recordException(t)
            SharedPreferencesSettings(oldPreferences)
        }
    }

    single<AppLocalizer>(createdAtStart = true) {
        AppLocalizer(
            context = get(),
            settingsRepository = get()
        )
    }
}
