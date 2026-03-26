package com.teEcclesia

import com.teEcclesia.di.apiModule
import com.teEcclesia.di.appModule
import com.teEcclesia.di.featureModule
import com.teEcclesia.di.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)

        modules(
            modules = appModule + apiModule + featureModule + networkModule
        )
    }
}