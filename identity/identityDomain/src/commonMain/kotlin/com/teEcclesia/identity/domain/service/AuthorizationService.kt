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

    suspend fun getRefreshToken(): String {
        return authenticationRepository.getAuthTokens()?.refreshToken ?: ""
    }

    suspend fun isRegistrationPending(): Boolean {
        return authenticationRepository.isRegistrationPending()
    }

    suspend fun getUserRole(): UserRole? = authenticationRepository.getUserRole()

    suspend fun saveUserRole(role: UserRole) = authenticationRepository.saveUserRole(role)

    suspend fun getUserStatus(): UserStatus? = authenticationRepository.getUserStatus()

    suspend fun saveUserStatus(status: UserStatus) = authenticationRepository.saveUserStatus(status)

    fun observeAccessToken(): StateFlow<String> = authenticationRepository.observeTokenChange()

    fun observeAuthState(): StateFlow<AuthState> = authenticationRepository.observeAuthState()
}
