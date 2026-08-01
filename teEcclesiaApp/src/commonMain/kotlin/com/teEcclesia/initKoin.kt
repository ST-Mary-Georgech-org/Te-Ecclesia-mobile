package com.teEcclesia

import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.PayloadData
import com.teEcclesia.di.apiModule
import com.teEcclesia.di.appModule
import com.teEcclesia.di.featureModule
import com.teEcclesia.di.networkModule
import com.teEcclesia.util.NotificationClickState
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    KMPNotifier.addListener(object : KMPNotifier.Listener {
        override fun onNotificationClicked(data: PayloadData) {
            NotificationClickState.onNotificationClicked(data)
        }
    })

    startKoin {
        config?.invoke(this)

        modules(
            modules = appModule + apiModule + featureModule + networkModule
        )
    }
}
