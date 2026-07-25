package com.teEcclesia.identity.data.di

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.repository.AuthenticationRepositoryImpl
import com.teEcclesia.identity.data.repository.ProfileRepositoryImpl
import com.teEcclesia.identity.data.repository.RegisterRepositoryImpl
import com.teEcclesia.identity.data.repository.ResetPasswordRepositoryImpl
import com.teEcclesia.identity.data.repository.SettingsRepositoryImpl
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.ProfileRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private const val COIL_CLIENT = "CoilClient"
private const val BASE_URL = "baseUrl"
private const val IDENTITY_SCOPE = "IdentityScope"

val identityDataModule = module {
    singleOf(::Settings)

    singleOf(::AuthenticationRepositoryImpl) bind AuthenticationRepository::class

    singleOf(::ResetPasswordRepositoryImpl) bind ResetPasswordRepository::class

    singleOf(::RegisterRepositoryImpl) bind RegisterRepository::class

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    singleOf(::AuthorizationService)
    single {
        provideHttpClient(
            baseUrl = get<String>(named(BASE_URL)),
            authorizationService = { get<AuthorizationService>() },
            settingsRepository = { get<SettingsRepository>() },
            isDebug = getOrNull(named("isDebug")) ?: false
        )
    }

    single(named(COIL_CLIENT)) {
        provideCoilClient()
    }

    single(named(IDENTITY_SCOPE)) { CoroutineScope(Dispatchers.Default) }
    includes(platformIdentityDataModule)
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}
