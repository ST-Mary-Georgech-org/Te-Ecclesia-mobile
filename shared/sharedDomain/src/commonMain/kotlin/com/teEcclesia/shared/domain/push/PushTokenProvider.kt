package com.teEcclesia.shared.domain.push

interface PushTokenProvider {
    suspend fun getToken(): String?
    suspend fun deleteToken()
}
