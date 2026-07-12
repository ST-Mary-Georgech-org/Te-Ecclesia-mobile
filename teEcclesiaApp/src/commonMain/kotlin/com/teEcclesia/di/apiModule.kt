package com.teEcclesia.di

import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.presentation.api.IdentityFeatureApiImpl
import com.teEcclesia.home.api.HomeFeatureApi
import com.teEcclesia.home.presentation.api.HomeFeatureApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiModule = module {
    singleOf(::IdentityFeatureApiImpl) bind IdentityFeatureApi::class
    singleOf(::HomeFeatureApiImpl) bind HomeFeatureApi::class
}
