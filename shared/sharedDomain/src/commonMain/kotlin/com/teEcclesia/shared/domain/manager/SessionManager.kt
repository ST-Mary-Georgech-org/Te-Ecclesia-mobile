package com.teEcclesia.shared.domain.manager

interface SessionManager {
    suspend fun onSessionExpired(message: String? = null)
    suspend fun onUserBlocked(message: String? = null)
}
