package com.teEcclesia.identity.data.repository

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.dataSource.local.setting.appLanguage
import com.teEcclesia.identity.data.dataSource.local.setting.appTheme
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.teEcclesia.identity.domain.model.CachedProfile
import com.teEcclesia.identity.data.dataSource.local.setting.cachedProfileJson
import kotlinx.serialization.json.Json
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.CachedProfileDto
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDomain
import com.teEcclesia.identity.data.dataSource.remote.dto.auth.response.toDto

class SettingsRepositoryImpl(
    private val settings: Settings,
) : SettingsRepository {
    private val _appLanguageFlow = MutableStateFlow(settings.appLanguage.toAppLanguage())
    private val _appThemeFlow = MutableStateFlow(settings.appTheme.toAppTheme())
    private val _cachedProfileFlow = MutableStateFlow(readCachedProfileFromSettings())

    private fun readCachedProfileFromSettings(): CachedProfile? {

        val cachedJson = settings.cachedProfileJson
        if (cachedJson.isBlank()) return null
        return try {
            Json.decodeFromString<CachedProfileDto>(cachedJson).toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun applyLanguage(appLanguage: AppLanguage) {
        settings.appLanguage = appLanguage.iso
        _appLanguageFlow.value = appLanguage
    }

    override fun observeAppLanguage(): StateFlow<AppLanguage> = _appLanguageFlow

    override fun getCurrentAppLanguage(): AppLanguage = settings.appLanguage.toAppLanguage()

    override suspend fun applyAppTheme(appTheme: AppTheme) {
        settings.appTheme = appTheme.name
        _appThemeFlow.value = appTheme
    }

    override fun observeAppTheme(): StateFlow<AppTheme> = _appThemeFlow

    override fun getCurrentTheme(): AppTheme = settings.appTheme.toAppTheme()

    override fun observeCachedProfile(): StateFlow<CachedProfile?> = _cachedProfileFlow.asStateFlow()

    override fun getCachedProfile(): CachedProfile? = _cachedProfileFlow.value

    override fun saveCachedProfile(profile: CachedProfile) {
        settings.cachedProfileJson = Json.encodeToString(profile.toDto())
        _cachedProfileFlow.value = profile
    }

    override fun clearCachedProfile() {
        settings.cachedProfileJson = ""
        _cachedProfileFlow.value = null
    }

    private fun String.toAppLanguage(): AppLanguage {
        return when (this) {
            AppLanguage.ENGLISH.iso -> AppLanguage.ENGLISH
            AppLanguage.ARABIC.iso -> AppLanguage.ARABIC
            else -> AppLanguage.DEFAULT
        }
    }

    private fun String.toAppTheme(): AppTheme {
        return when (this) {
            AppTheme.DARK.name -> AppTheme.DARK
            AppTheme.LIGHT.name -> AppTheme.LIGHT
            else -> AppTheme.SYSTEM
        }
    }
}
