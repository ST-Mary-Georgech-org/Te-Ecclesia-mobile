package com.teEcclesia.identity.data.repository

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.dataSource.local.setting.accessToken
import com.teEcclesia.identity.data.dataSource.local.setting.refreshToken
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.RefreshRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.UpdateDeviceTokenRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.shared.data.shared.BaseGateway
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
import com.teEcclesia.identity.domain.model.AuthenticationTokens
import com.teEcclesia.identity.domain.model.LoginRequest
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthenticationRepositoryImpl(
    client: HttpClient,
    private val settings: Settings,
) : BaseGateway(client), AuthenticationRepository {

    private val observableToken: MutableStateFlow<String> = MutableStateFlow(getInitialToken())

    private fun getInitialToken(): String = settings.accessToken

    override suspend fun login(request: LoginRequest) {
        val response = tryToExecute<AuthenticationResponse> {
            post(LOGIN_ENDPOINT) {
                setBody(request.toDto())
            }
        }

        saveAuthTokens(response.toDomain())
        client.invalidateAuthTokens()
    }

    override suspend fun logout() {
        tryToExecute<Unit> {
            post(LOGOUT_ENDPOINT) {
                setBody(RefreshRequestDto(settings.refreshToken))
            }
        }
        client.invalidateAuthTokens()
        clearAuthTokens()
    }

    override suspend fun refreshAccessToken(): String {
        val response = tryToExecute<AuthenticationResponse> {
            post(REFRESH_ENDPOINT) {
                setBody(RefreshRequestDto(settings.refreshToken))
            }
        }
        saveTokens(response.toDomain())
        client.invalidateAuthTokens()
        return settings.accessToken
    }

    override suspend fun getAccessToken(): String = settings.accessToken

    override suspend fun getAuthTokens(): AuthenticationTokens? =
        createAuthTokensIfValid(settings.accessToken, settings.refreshToken)

    private fun createAuthTokensIfValid(
        accessToken: String,
        refreshToken: String
    ): AuthenticationTokens? =
        AuthenticationTokens(accessToken, refreshToken).takeIf {
            accessToken.isNotBlank() && refreshToken.isNotBlank()
        }

    override suspend fun clearAuthTokens() {
        saveTokensToSettings(AuthenticationTokens(accessToken = "", refreshToken = ""))
        emitToken("")
    }

    override suspend fun updateDeviceToken(deviceToken: String) {
        val refreshToken = settings.refreshToken
        if (refreshToken.isNotBlank()) {
            tryToExecute<Unit> {
                patch(DEVICE_TOKEN_ENDPOINT) {
                    setBody(UpdateDeviceTokenRequestDto(refreshToken = refreshToken, deviceToken = deviceToken))
                }
            }
        }
    }

    override fun observeTokenChange(): StateFlow<String> = observableToken

    override suspend fun saveAuthTokens(authTokens: AuthenticationTokens) {
        saveTokens(authTokens)
    }

    private suspend fun saveTokens(authTokens: AuthenticationTokens, shouldEmit: Boolean = true) {
        saveTokensToSettings(authTokens)
        if (shouldEmit) {
            emitToken(authTokens.accessToken)
        }
    }

    private suspend fun emitToken(token: String) {
        observableToken.emit(token)
    }

    private fun saveTokensToSettings(authTokens: AuthenticationTokens) {
        settings.accessToken = authTokens.accessToken
        settings.refreshToken = authTokens.refreshToken
    }

    companion object {
        const val LOGIN_ENDPOINT = "api/v1/identity/auth/login"
        const val REFRESH_ENDPOINT = "api/v1/identity/auth/refresh"
        const val LOGOUT_ENDPOINT = "api/v1/identity/auth/logout"
        const val DEVICE_TOKEN_ENDPOINT = "api/v1/identity/auth/device-token"
    }
}
