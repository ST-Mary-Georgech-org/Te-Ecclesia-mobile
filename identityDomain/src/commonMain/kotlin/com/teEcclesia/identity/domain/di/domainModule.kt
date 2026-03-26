package com.teEcclesia.identity.domain.di

import com.teEcclesia.identity.domain.service.AuthorizationService
import com.teEcclesia.identity.domain.useCase.validation.auth.ValidationUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::AuthorizationService)
    singleOf(::ValidationUseCase)
}