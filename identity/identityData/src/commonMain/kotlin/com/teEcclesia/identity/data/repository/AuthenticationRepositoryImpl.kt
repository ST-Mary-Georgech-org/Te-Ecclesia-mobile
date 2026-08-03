package com.teEcclesia.identity.data.repository

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.dataSource.local.setting.accessToken
import com.teEcclesia.identity.data.dataSource.local.setting.canApproveRequests
import com.teEcclesia.identity.data.dataSource.local.setting.khademStageId
import com.teEcclesia.identity.data.dataSource.local.setting.khademYearId
import com.teEcclesia.identity.data.dataSource.local.setting.refreshToken
import com.teEcclesia.identity.data.dataSource.local.setting.responsibleStageIds
import com.teEcclesia.identity.data.dataSource.local.setting.responsibleYearIds
import com.teEcclesia.identity.data.dataSource.local.setting.userRole
import com.teEcclesia.identity.data.dataSource.local.setting.userStatus
import com.teEcclesia.identity.data.dataSource.local.setting.cachedProfileJson
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.RefreshRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.UpdateDeviceTokenRequestDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.request.toDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.AuthenticationResponse
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.identity.data.utils.invalidateAuthTokens
import com.teEcclesia.identity.domain.model.AuthState
import com.teEcclesia.identity.domain.model.AuthenticationTokens
import com.teEcclesia.identity.domain.model.LoginRequest
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.shared.data.shared.BaseRepository
import com.teEcclesia.shared.domain.exception.UnAuthorizedException
import com.teEcclesia.shared.domain.exception.UserIsBlockedException
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.push.firebase.firebasePushNotifier
import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

import com.teEcclesia.identity.domain.repository.SettingsRepository

class AuthenticationRepositoryImpl(
    client: HttpClient,
    private val settings: Settings,
    private val settingsRepository: SettingsRepository,
) : BaseRepository(client), AuthenticationRepository {

    private val observableToken: MutableStateFlow<String> = MutableStateFlow(getInitialToken())
    private val observableAuthState: MutableStateFlow<AuthState> =
        MutableStateFlow(getInitialAuthState())
    private val observableRequestsAccess: MutableStateFlow<Boolean> =
        MutableStateFlow(calculateRequestsAccess())
    private val observableAttendanceAccess: MutableStateFlow<Boolean> =
        MutableStateFlow(calculateAttendanceAccess())

    private fun calculateRequestsAccess(): Boolean {
        val roleStr = settings.userRole
        val role =
            if (roleStr.isBlank()) null else runCatching { UserRole.valueOf(roleStr) }.getOrNull()
        val canApprove = settings.canApproveRequests
        return role == UserRole.ADMIN || (role == UserRole.KHADEM && canApprove)
    }

    private fun calculateAttendanceAccess(): Boolean {
        val roleStr = settings.userRole
        val role =
            if (roleStr.isBlank()) null else runCatching { UserRole.valueOf(roleStr) }.getOrNull()
        return role == UserRole.ADMIN || role == UserRole.KHADEM
    }

    private fun updateRequestsAccess() {
        observableRequestsAccess.value = calculateRequestsAccess()
        observableAttendanceAccess.value = calculateAttendanceAccess()
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
        val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
        val response = tryToExecute<AuthenticationResponse> {
            post(LOGIN_ENDPOINT) {
                setBody(request.toDto(deviceToken))
            }
        }

        settingsRepository.clearCachedProfile()
        saveTokens(response.toDomain(), syncDeviceToken = false)
        client.invalidateAuthTokens()
    }

    override suspend fun logout() {
        val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
        withContext(NonCancellable) {
            tryToExecute<Unit> {
                post(LOGOUT_ENDPOINT) {
                    setBody(RefreshRequestDto(settings.refreshToken, deviceToken))
                }
            }
            clearAuthState()
        }
    }

    override suspend fun refreshAccessToken(): String {
        return withContext(NonCancellable) {
            try {
                val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
                val response = tryToExecute<AuthenticationResponse> {
                    post(REFRESH_ENDPOINT) {
                        setBody(RefreshRequestDto(settings.refreshToken, deviceToken))
                    }
                }
                saveTokens(response.toDomain(), syncDeviceToken = false)
                client.invalidateAuthTokens()
                settings.accessToken
            } catch (e: UnAuthorizedException) {
                clearAuthState()
                throw e
            } catch (e: UserIsBlockedException) {
                clearAuthState()
                throw e
            }
        }
    }

    private suspend fun clearAuthState() {
        client.invalidateAuthTokens()
        clearAuthTokens()
    }

    override suspend fun refreshRegistrationToken(): String {
        val deviceToken = KMPNotifier.firebasePushNotifier.getToken()
        val response = tryToExecute<AuthenticationResponse> {
            post(REFRESH_REGISTRATION_ENDPOINT) {
                setBody(RefreshRequestDto(settings.refreshToken, deviceToken))
            }
        }
        saveRegistrationToken(response.accessToken, response.refreshToken, syncDeviceToken = false)
        return response.accessToken
    }

    override suspend fun upgradeRegistrationToken(): String {
        return withContext(NonCancellable) {
            val response = tryToExecute<AuthenticationResponse> {
                post(UPGRADE_REGISTRATION_TOKEN_ENDPOINT)
            }
            saveTokens(response.toDomain(), syncDeviceToken = true)
            client.invalidateAuthTokens()
            settings.accessToken
        }
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
        settings.khademStageId = -1L
        settings.khademYearId = -1L
        settings.responsibleStageIds = ""
        settings.responsibleYearIds = ""
        settingsRepository.clearCachedProfile()
        emitToken("")
        observableAuthState.emit(AuthState.UNAUTHENTICATED)
        updateRequestsAccess()
    }

    override suspend fun saveUserRole(role: UserRole) {
        if (settings.userRole != role.name) {
            settings.userRole = role.name
            updateRequestsAccess()
        }
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
        if (settings.userStatus != status.name) {
            settings.userStatus = status.name
        }
    }

    override fun getUserStatus(): UserStatus? {
        val statusStr = settings.userStatus
        if (statusStr.isBlank()) return null
        return runCatching { UserStatus.valueOf(statusStr) }.getOrNull()
    }

    override suspend fun saveCanApproveRequests(canApprove: Boolean) {
        if (settings.canApproveRequests != canApprove) {
            settings.canApproveRequests = canApprove
            updateRequestsAccess()
        }
    }

    override fun getKhademStageId(): Long? {
        val id = settings.khademStageId
        return if (id == -1L) null else id
    }

    override fun getKhademYearId(): Long? {
        val id = settings.khademYearId
        return if (id == -1L) null else id
    }

    override fun getResponsibleStageIds(): List<Long> {
        val str = settings.responsibleStageIds
        if (str.isBlank()) return emptyList()
        return str.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    override fun getResponsibleYearIds(): List<Long> {
        val str = settings.responsibleYearIds
        if (str.isBlank()) return emptyList()
        return str.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    override fun saveKhademAuthorizationDetails(
        stageId: Long?,
        yearId: Long?,
        responsibleStageIds: List<Long>,
        responsibleYearIds: List<Long>
    ) {
        settings.khademStageId = stageId ?: -1L
        settings.khademYearId = yearId ?: -1L
        settings.responsibleStageIds = responsibleStageIds.joinToString(",")
        settings.responsibleYearIds = responsibleYearIds.joinToString(",")
    }

    override suspend fun getCanApproveRequests(): Boolean {
        return settings.canApproveRequests
    }

    override suspend fun saveRegistrationToken(token: String, refreshToken: String, syncDeviceToken: Boolean) {
        settings.accessToken = token
        if (refreshToken.isNotBlank()) {
            settings.refreshToken = refreshToken
        }
        settings.userStatus = UserStatus.PROFILE_INCOMPLETE.name
        client.invalidateAuthTokens()
        emitToken(token)
        observableAuthState.emit(
            if (token.isNotBlank()) AuthState.REGISTRATION_PENDING else AuthState.UNAUTHENTICATED
        )
        if (syncDeviceToken && token.isNotBlank() && refreshToken.isNotBlank()) {
            syncDeviceTokenIfAvailable()
        }
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
            try {
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
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
        }
    }

    override fun observeTokenChange(): StateFlow<String> = observableToken

    override fun observeAuthState(): StateFlow<AuthState> = observableAuthState

    override fun observeRequestsAccess(): StateFlow<Boolean> = observableRequestsAccess

    override fun observeAttendanceAccess(): StateFlow<Boolean> = observableAttendanceAccess

    override suspend fun saveAuthTokens(authTokens: AuthenticationTokens, syncDeviceToken: Boolean) {
        saveTokens(authTokens, syncDeviceToken = syncDeviceToken)
    }

    private suspend fun saveTokens(authTokens: AuthenticationTokens, shouldEmit: Boolean = true, syncDeviceToken: Boolean = true) {
        saveTokensToSettings(authTokens)
        if (shouldEmit) {
            emitToken(authTokens.accessToken)
            observableAuthState.emit(
                if (authTokens.accessToken.isNotBlank()) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
            )
        }
        if (syncDeviceToken && authTokens.accessToken.isNotBlank() && authTokens.refreshToken.isNotBlank()) {
            syncDeviceTokenIfAvailable()
        }
    }

    private suspend fun syncDeviceTokenIfAvailable() {
        val deviceToken = runCatching { KMPNotifier.firebasePushNotifier.getToken() }.getOrNull()
        if (!deviceToken.isNullOrBlank()) {
            updateDeviceToken(deviceToken)
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
        const val UPGRADE_REGISTRATION_TOKEN_ENDPOINT = "api/v1/identity/auth/upgrade-registration-token"
        const val LOGOUT_ENDPOINT = "api/v1/identity/auth/logout"
        const val DEVICE_TOKEN_ENDPOINT = "api/v1/identity/auth/device-token"
    }
}
