package com.teEcclesia.di

import com.teEcclesia.AppEnvironment
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BASE_URL = "baseUrl"

val networkModule = module {
    single(named(BASE_URL)) { AppEnvironment.baseUrl }
}