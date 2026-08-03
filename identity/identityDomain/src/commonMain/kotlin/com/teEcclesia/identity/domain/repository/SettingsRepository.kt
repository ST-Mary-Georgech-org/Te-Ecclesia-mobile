package com.teEcclesia.identity.domain.repository

import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import kotlinx.coroutines.flow.StateFlow
import com.teEcclesia.identity.domain.model.CachedProfile

interface SettingsRepository {
    suspend fun applyLanguage(appLanguage: AppLanguage)
    fun observeAppLanguage(): StateFlow<AppLanguage>
    fun getCurrentAppLanguage(): AppLanguage
    suspend fun applyAppTheme(appTheme: AppTheme)
    fun observeAppTheme(): StateFlow<AppTheme>
    fun getCurrentTheme(): AppTheme
    fun observeCachedProfile(): StateFlow<CachedProfile?>
    fun getCachedProfile(): CachedProfile?
    fun saveCachedProfile(profile: CachedProfile)
    fun clearCachedProfile()
}
