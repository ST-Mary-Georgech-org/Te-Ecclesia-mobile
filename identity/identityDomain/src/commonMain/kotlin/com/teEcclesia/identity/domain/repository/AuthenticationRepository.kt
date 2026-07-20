package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.model.LoginRequest
import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.model.AuthenticationTokens

interface AuthenticationRepository {
    suspend fun login(request: LoginRequest)
    suspend fun logout()
    suspend fun refreshAccessToken(): String
    suspend fun getAccessToken(): String
    suspend fun getAuthTokens(): AuthenticationTokens?
    suspend fun saveAuthTokens(authTokens: AuthenticationTokens)
    suspend fun clearAuthTokens()
    suspend fun updateDeviceToken(deviceToken: String)
    fun observeTokenChange(): StateFlow<String>
}
