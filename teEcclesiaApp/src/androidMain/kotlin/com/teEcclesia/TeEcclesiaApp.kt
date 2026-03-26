package com.teEcclesia

import android.app.Application
import org.koin.android.ext.koin.androidContext

class TeEcclesiaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin{
            androidContext(this@TeEcclesiaApp)
        }
    }
}