package com.teEcclesia.identity.domain.service

import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.repository.AuthenticationRepository

class AuthorizationService(private val authenticationRepository: AuthenticationRepository) {

    suspend fun getAccessToken(): String {
        return authenticationRepository.getAccessToken()
    }

    suspend fun getNewAccessToken(): String {
        return authenticationRepository.refreshAccessToken()
    }

    suspend fun getRefreshToken(): String {
        return authenticationRepository.getAuthTokens()?.refreshToken ?: ""
    }

    fun observeAccessToken(): StateFlow<String> = authenticationRepository.observeTokenChange()
}