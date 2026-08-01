package com.teEcclesia.identity.domain.service

import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

import com.teEcclesia.identity.domain.model.AuthState
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserStatus

class AuthorizationService(private val authenticationRepository: AuthenticationRepository) {

    suspend fun getAccessToken(): String {
        return authenticationRepository.getAccessToken()
    }

    suspend fun getNewAccessToken(): String {
        return authenticationRepository.refreshAccessToken()
    }

    suspend fun getNewRegistrationToken(): String {
        return authenticationRepository.refreshRegistrationToken()
    }

    suspend fun upgradeRegistrationToken(): String {
        return authenticationRepository.upgradeRegistrationToken()
    }

    suspend fun getRefreshToken(): String {
        return authenticationRepository.getAuthTokens()?.refreshToken ?: ""
    }

    suspend fun clearAuthTokens() {
        authenticationRepository.clearAuthTokens()
    }

    suspend fun isRegistrationPending(): Boolean {
        return authenticationRepository.isRegistrationPending()
    }

    suspend fun getUserRole(): UserRole? = authenticationRepository.getUserRole()

    suspend fun saveUserRole(role: UserRole) = authenticationRepository.saveUserRole(role)

    fun getUserStatus(): UserStatus? = authenticationRepository.getUserStatus()

    suspend fun saveUserStatus(status: UserStatus) = authenticationRepository.saveUserStatus(status)

    suspend fun canApproveRequests(): Boolean = authenticationRepository.getCanApproveRequests()
    
    suspend fun saveCanApproveRequests(canApprove: Boolean) = authenticationRepository.saveCanApproveRequests(canApprove)

    suspend fun getKhademStageId(): Long? = authenticationRepository.getKhademStageId()

    suspend fun getKhademYearId(): Long? = authenticationRepository.getKhademYearId()

    suspend fun getResponsibleStageIds(): List<Long> = authenticationRepository.getResponsibleStageIds()

    suspend fun getResponsibleYearIds(): List<Long> = authenticationRepository.getResponsibleYearIds()

    suspend fun saveKhademAuthorizationDetails(
        stageId: Long?,
        yearId: Long?,
        responsibleStageIds: List<Long>,
        responsibleYearIds: List<Long>
    ) {
        authenticationRepository.saveKhademAuthorizationDetails(stageId, yearId, responsibleStageIds, responsibleYearIds)
    }

    suspend fun canSearchUsers(): Boolean {
        val role = getUserRole()
        if (role == UserRole.ADMIN) return true
        if (role == UserRole.KHADEM) {
            return getKhademStageId() != null || getKhademYearId() != null
        }
        return false
    }

    suspend fun canAddStudent(): Boolean {
        val role = getUserRole()
        if (role == UserRole.ADMIN) return true
        if (role == UserRole.KHADEM) {
            return getResponsibleStageIds().isNotEmpty() || getResponsibleYearIds().isNotEmpty()
        }
        return false
    }

    suspend fun hasRegistrationRequestsAccess(): Boolean {
        val role = getUserRole()
        return role == UserRole.ADMIN || (role == UserRole.KHADEM && canApproveRequests())
    }

    suspend fun hasAttendanceAccess(): Boolean {
        val role = getUserRole()
        return role == UserRole.ADMIN || role == UserRole.KHADEM
    }

    fun observeAccessToken(): StateFlow<String> = authenticationRepository.observeTokenChange()

    fun observeAuthState(): StateFlow<AuthState> = authenticationRepository.observeAuthState()

    fun observeRequestsAccess(): StateFlow<Boolean> = authenticationRepository.observeRequestsAccess()

    fun observeAttendanceAccess(): StateFlow<Boolean> = authenticationRepository.observeAttendanceAccess()
}
