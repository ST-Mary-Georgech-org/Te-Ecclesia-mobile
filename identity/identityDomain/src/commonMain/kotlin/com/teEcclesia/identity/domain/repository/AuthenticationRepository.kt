package com.teEcclesia.identity.domain.repository

import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.model.AuthenticationTokens

interface AuthenticationRepository {
    suspend fun login(username: String, password: String)
    suspend fun logout()
    suspend fun refreshAccessToken(): String
    suspend fun getAccessToken(): String
    suspend fun getAuthTokens(): AuthenticationTokens?
    suspend fun saveAuthTokens(authTokens: AuthenticationTokens)
    suspend fun clearAuthTokens()
    fun observeTokenChange(): StateFlow<String>
}
