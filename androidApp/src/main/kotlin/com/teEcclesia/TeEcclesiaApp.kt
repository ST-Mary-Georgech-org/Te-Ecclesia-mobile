package com.teEcclesia

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext

class TeEcclesiaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MMKV.initialize(this)

        AppEnvironment.internalBaseUrl = BuildConfig.BASE_URL
        AppEnvironment.internalVersionName = BuildConfig.VERSION_NAME
        AppEnvironment.internalIsDebug = BuildConfig.DEBUG

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        KMPNotifier.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.logo,
                showPushNotification = false
            ),
            FirebasePush
        )

        initKoin {
            androidContext(this@TeEcclesiaApp)
        }
    }
}
