package com.teEcclesia

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.teEcclesia.notifications.data.push.PushNotificationsInitializer
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext

class TeEcclesiaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        try {
            MMKV.initialize(this)
        } catch (t: Throwable) {
            android.util.Log.e("TeEcclesiaApp", "Failed to initialize MMKV native library", t)
            FirebaseCrashlytics.getInstance().recordException(t)
        }

        val is32BitOS = !android.os.Process.is64Bit()
        val activityManager = getSystemService(ACTIVITY_SERVICE) as? android.app.ActivityManager
        val isLowRam = activityManager?.isLowRamDevice == true

        if (is32BitOS || isLowRam) {
            System.setProperty("org.jetbrains.skiko.renderApi", "OPENGL")
        }

        AppEnvironment.internalBaseUrl = BuildConfig.BASE_URL
        AppEnvironment.internalVersionName = BuildConfig.VERSION_NAME
        AppEnvironment.internalIsDebug = BuildConfig.DEBUG

        initKoin {
            androidContext(this@TeEcclesiaApp)
        }

        PushNotificationsInitializer.initialize(showPushNotification = true)
    }
}
