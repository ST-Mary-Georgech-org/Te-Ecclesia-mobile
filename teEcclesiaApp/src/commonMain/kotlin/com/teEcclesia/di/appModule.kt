package com.teEcclesia.di

import com.teEcclesia.appEntryPoint.MainEntryViewModel
import com.teEcclesia.AppEnvironment
import com.teEcclesia.designsystem.navigation.ResultStore
import com.teEcclesia.designsystem.navigation.SnackBarManager
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.designsystem.navigation.effector.EffectorImpl
import com.teEcclesia.designsystem.navigation.getDispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val APP_VERSION = "appVersion"
val appModule = module {
    single(named(APP_VERSION)) { AppEnvironment.versionName }
    single { SnackBarManager() }
    single { getDispatcherProvider() }
    viewModelOf(::MainEntryViewModel)
    singleOf(::EffectorImpl) bind Effector::class
    singleOf(::ResultStore)
}
