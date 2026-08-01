package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.LoginRequest
import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.model.AuthenticationTokens

import com.teEcclesia.identity.domain.model.AuthState
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus

interface AuthenticationRepository {
    suspend fun login(request: LoginRequest)
    suspend fun logout()
    suspend fun refreshAccessToken(): String
    suspend fun refreshRegistrationToken(): String
    suspend fun upgradeRegistrationToken(): String
    suspend fun getAccessToken(): String
    suspend fun getAuthTokens(): AuthenticationTokens?
    suspend fun saveAuthTokens(authTokens: AuthenticationTokens)
    suspend fun saveRegistrationToken(token: String, refreshToken: String = "")
    suspend fun isRegistrationPending(): Boolean
    suspend fun clearAuthTokens()
    suspend fun saveUserRole(role: UserRole)
    suspend fun getUserRole(): UserRole?
    suspend fun saveUserStatus(status: UserStatus)
    fun getUserStatus(): UserStatus?
    suspend fun saveCanApproveRequests(canApprove: Boolean)
    suspend fun getCanApproveRequests(): Boolean
    fun getKhademStageId(): Long?
    fun getKhademYearId(): Long?
    fun getResponsibleStageIds(): List<Long>
    fun getResponsibleYearIds(): List<Long>
    fun saveKhademAuthorizationDetails(stageId: Long?, yearId: Long?, responsibleStageIds: List<Long>, responsibleYearIds: List<Long>)
    suspend fun updateDeviceToken(deviceToken: String)
    fun observeTokenChange(): StateFlow<String>
    fun observeAuthState(): StateFlow<AuthState>
    fun observeRequestsAccess(): StateFlow<Boolean>
    fun observeAttendanceAccess(): StateFlow<Boolean>
}
