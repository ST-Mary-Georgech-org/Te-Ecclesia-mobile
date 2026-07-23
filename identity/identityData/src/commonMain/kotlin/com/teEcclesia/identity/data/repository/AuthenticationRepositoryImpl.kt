package com.teEcclesia.identity.data.repository

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.dataSource.local.setting.accessToken
import com.teEcclesia.identity.data.dataSource.local.setting.refreshToken
import com.teEcclesia.identity.data.dataSource.local.setting.userRole
import com.teEcclesia.identity.data.dataSource.local.setting.userStatus
import com.teEcclesia.identity.data.dataSource.local.setting.canApproveRequests
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.RefreshRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.UpdateDeviceTokenRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
import com.teEcclesia.identity.domain.model.AuthState
import com.teEcclesia.identity.domain.model.AuthenticationTokens
import com.teEcclesia.identity.domain.model.LoginRequest
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.shared.data.shared.BaseRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthenticationRepositoryImpl(
    client: HttpClient,
    private val settings: Settings,
) : BaseRepository(client), AuthenticationRepository {

    private val observableToken: MutableStateFlow<String> = MutableStateFlow(getInitialToken())
    private val observableAuthState: MutableStateFlow<AuthState> = MutableStateFlow(getInitialAuthState())
    private val observableRequestsAccess: MutableStateFlow<Boolean> = MutableStateFlow(calculateRequestsAccess())

    private fun calculateRequestsAccess(): Boolean {
        val roleStr = settings.userRole
        val role = if (roleStr.isBlank()) null else runCatching { UserRole.valueOf(roleStr) }.getOrNull()
        val canApprove = settings.canApproveRequests
        return role == UserRole.ADMIN || (role == UserRole.KHADEM && canApprove)
    }

    private fun updateRequestsAccess() {
        observableRequestsAccess.value = calculateRequestsAccess()
    }

    private fun getInitialToken(): String = settings.accessToken

    private fun getInitialAuthState(): AuthState {
        if (settings.accessToken.isBlank()) return AuthState.UNAUTHENTICATED
        val statusStr = settings.userStatus
        val status = runCatching { UserStatus.valueOf(statusStr) }.getOrNull()
        return when (status) {
            UserStatus.APPROVED -> AuthState.AUTHENTICATED
            UserStatus.PENDING_APPROVAL,
            UserStatus.PROFILE_INCOMPLETE,
            UserStatus.UNVERIFIED -> AuthState.REGISTRATION_PENDING
            else -> if (statusStr.isBlank()) AuthState.AUTHENTICATED else AuthState.REGISTRATION_PENDING
        }
    }

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

    override suspend fun refreshRegistrationToken(): String {
        val response = tryToExecute<AuthenticationResponse> {
            post(REFRESH_REGISTRATION_ENDPOINT) {
                setBody(RefreshRequestDto(settings.refreshToken))
            }
        }
        saveRegistrationToken(response.accessToken, response.refreshToken)
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
        settings.userRole = ""
        settings.userStatus = ""
        settings.canApproveRequests = false
        emitToken("")
        observableAuthState.emit(AuthState.UNAUTHENTICATED)
        updateRequestsAccess()
    }

    override suspend fun saveUserRole(role: UserRole) {
        settings.userRole = role.name
        updateRequestsAccess()
    }

    override suspend fun getUserRole(): UserRole? {
        val roleStr = settings.userRole
        if (roleStr.isBlank()) return null
        return try {
            UserRole.valueOf(roleStr)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveUserStatus(status: UserStatus) {
        settings.userStatus = status.name
    }

    override suspend fun getUserStatus(): UserStatus? {
        val statusStr = settings.userStatus
        if (statusStr.isBlank()) return null
        return runCatching { UserStatus.valueOf(statusStr) }.getOrNull()
    }

    override suspend fun saveCanApproveRequests(canApprove: Boolean) {
        settings.canApproveRequests = canApprove
        updateRequestsAccess()
    }

    override suspend fun getCanApproveRequests(): Boolean {
        return settings.canApproveRequests
    }

    override suspend fun saveRegistrationToken(token: String, refreshToken: String) {
        settings.accessToken = token
        settings.refreshToken = refreshToken
        settings.userStatus = UserStatus.PROFILE_INCOMPLETE.name
        client.invalidateAuthTokens()
        emitToken(token)
        observableAuthState.emit(AuthState.REGISTRATION_PENDING)
    }

    override suspend fun isRegistrationPending(): Boolean {
        val status = getUserStatus()
        return status == UserStatus.PROFILE_INCOMPLETE ||
               status == UserStatus.UNVERIFIED ||
               status == UserStatus.PENDING_APPROVAL
    }

    override suspend fun updateDeviceToken(deviceToken: String) {
        val refreshToken = settings.refreshToken
        if (refreshToken.isNotBlank()) {
            tryToExecute<Unit> {
                patch(DEVICE_TOKEN_ENDPOINT) {
                    setBody(
                        UpdateDeviceTokenRequestDto(
                            refreshToken = refreshToken,
                            deviceToken = deviceToken
                        )
                    )
                }
            }
        }
    }

    override fun observeTokenChange(): StateFlow<String> = observableToken

    override fun observeAuthState(): StateFlow<AuthState> = observableAuthState

    override fun observeRequestsAccess(): StateFlow<Boolean> = observableRequestsAccess

    override suspend fun saveAuthTokens(authTokens: AuthenticationTokens) {
        saveTokens(authTokens)
    }

    private suspend fun saveTokens(authTokens: AuthenticationTokens, shouldEmit: Boolean = true) {
        saveTokensToSettings(authTokens)
        if (shouldEmit) {
            emitToken(authTokens.accessToken)
            observableAuthState.emit(
                if (authTokens.accessToken.isNotBlank()) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
            )
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
        const val REFRESH_REGISTRATION_ENDPOINT = "api/v1/identity/auth/refresh-registration"
        const val LOGOUT_ENDPOINT = "api/v1/identity/auth/logout"
        const val DEVICE_TOKEN_ENDPOINT = "api/v1/identity/auth/device-token"
    }
}
