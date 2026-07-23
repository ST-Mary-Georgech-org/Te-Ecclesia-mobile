package com.teEcclesia.identity.domain.di

import com.teEcclesia.identity.domain.service.AuthorizationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::AuthorizationService)
}
