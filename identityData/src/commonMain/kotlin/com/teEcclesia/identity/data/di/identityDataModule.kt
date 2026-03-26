package com.teEcclesia.identity.data.di

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.repository.AuthenticationRepositoryImpl
import com.teEcclesia.identity.data.repository.RegisterRepositoryImpl
import com.teEcclesia.identity.data.repository.ResetPasswordRepositoryImpl
import com.teEcclesia.identity.data.repository.SettingsRepositoryImpl
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.RegisterRepository
import com.teEcclesia.identity.domain.repository.ResetPasswordRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val IDENTITY_CLIENT = "IdentityClient"
private const val COIL_CLIENT = "CoilClient"
private const val BASE_URL = "baseUrl"
private const val IDENTITY_SCOPE = "IdentityScope"

val identityDataModule = module {
    single { CIO.create() }
    singleOf(::Settings)

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            client = get(named(IDENTITY_CLIENT)),
            settings = get(),
        )
    }

    single<ResetPasswordRepository> {
        ResetPasswordRepositoryImpl(client = get(named(IDENTITY_CLIENT)))
    }

    single<RegisterRepository> {
        RegisterRepositoryImpl(
            client = get(named(IDENTITY_CLIENT)),
            authenticationRepository = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(settings = get())
    }

    singleOf(::AuthorizationService)
    single(named(IDENTITY_CLIENT)) {
        provideHttpClient(
            engine = get(),
            baseUrl = get<String>(named(BASE_URL)),
            authorizationService = { get<AuthorizationService>() },
        )
    }

    single(named(COIL_CLIENT)) {
        provideCoilClient(engine = get())
    }

    single(named(IDENTITY_SCOPE)) { CoroutineScope(Dispatchers.IO) }
}