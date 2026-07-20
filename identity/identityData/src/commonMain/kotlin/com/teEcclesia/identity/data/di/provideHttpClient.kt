package com.teEcclesia.identity.data.di

import com.teEcclesia.identity.data.repository.AuthenticationRepositoryImpl.Companion.LOGIN_ENDPOINT
import com.teEcclesia.identity.data.repository.AuthenticationRepositoryImpl.Companion.REFRESH_ENDPOINT
import com.teEcclesia.identity.data.repository.RegisterRepositoryImpl.Companion.REGISTER
import com.teEcclesia.identity.data.repository.ResetPasswordRepositoryImpl.Companion.RESET_PASSWORD
import com.teEcclesia.identity.data.repository.ResetPasswordRepositoryImpl.Companion.RESET_PASSWORD_REQUEST_OTP
import com.teEcclesia.identity.data.repository.ResetPasswordRepositoryImpl.Companion.RESET_PASSWORD_VERIFY_OTP
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.service.AuthorizationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

internal fun provideHttpClient(
    baseUrl: String,
    authorizationService: suspend () -> AuthorizationService,
    settingsRepository: () -> SettingsRepository,
): HttpClient {
    return createHttpClient {
        expectSuccess = true

        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        install(languageThemeInterceptor(settingsRepository))

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("Identity Client: $message")
                }
            }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = authorizationService().getAccessToken(),
                        refreshToken = authorizationService().getRefreshToken(),
                    )
                }
                refreshTokens {
                    val currentRefreshToken = authorizationService().getRefreshToken()
                    if (currentRefreshToken.isBlank()) {
                        return@refreshTokens null
                    }

                    return@refreshTokens try {
                        BearerTokens(
                            accessToken = authorizationService().getNewAccessToken(),
                            refreshToken = currentRefreshToken,
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (_: Exception) {
                        null
                    }
                }
                sendWithoutRequest { request ->
                    val path = request.url.encodedPath.removePrefix("/")
                    path !in whiteListEndPoints
                }
            }
        }
        install(HttpTimeout) {
            connectTimeoutMillis = NETWORK_TIMEOUT_MS
            requestTimeoutMillis = NETWORK_TIMEOUT_MS
        }
    }
}

internal fun provideCoilClient(): HttpClient {
    return createHttpClient {
        install(HttpTimeout) {
            connectTimeoutMillis = NETWORK_TIMEOUT_MS
            requestTimeoutMillis = NETWORK_TIMEOUT_MS
        }
    }
}

const val NETWORK_TIMEOUT_MS = 15_000L
private val whiteListEndPoints = listOf(
    LOGIN_ENDPOINT,
    REFRESH_ENDPOINT,
    RESET_PASSWORD_REQUEST_OTP,
    RESET_PASSWORD_VERIFY_OTP,
    RESET_PASSWORD,
    REGISTER
)
