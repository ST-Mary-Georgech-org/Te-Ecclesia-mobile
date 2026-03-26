package com.teEcclesia.identity.data.repository

import com.russhwolf.settings.Settings
import com.teEcclesia.identity.data.dataSource.local.setting.appLanguage
import com.teEcclesia.identity.data.dataSource.local.setting.appTheme
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepositoryImpl(
    private val settings: Settings,
) : SettingsRepository {
    private val _appLanguageFlow = MutableStateFlow(settings.appLanguage.toAppLanguage())
    private val _appThemeFlow = MutableStateFlow(settings.appTheme.toAppTheme())

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