package com.teEcclesia

import android.app.Application
import org.koin.android.ext.koin.androidContext

class TeEcclesiaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        AppEnvironment.internalBaseUrl = BuildConfig.BASE_URL
        AppEnvironment.internalVersionName = BuildConfig.VERSION_NAME
        AppEnvironment.internalIsDebug = BuildConfig.DEBUG

        initKoin {
            androidContext(this@TeEcclesiaApp)
        }
    }
}
