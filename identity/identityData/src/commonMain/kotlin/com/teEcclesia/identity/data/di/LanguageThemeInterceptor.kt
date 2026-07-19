package com.teEcclesia.identity.data.di

import com.teEcclesia.identity.data.utils.isSystemDarkTheme
import com.teEcclesia.identity.data.utils.languageCode
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import io.ktor.client.plugins.api.createClientPlugin

fun languageThemeInterceptor(settingsRepository: () -> SettingsRepository) = createClientPlugin("LanguageThemeInterceptor") {
    onRequest { request, _ ->
        if (!request.headers.contains("Accept-Language")) {
            val lang = settingsRepository().getCurrentAppLanguage()
            val resolvedLang = lang.toLanguageCode()
            request.headers.append("Accept-Language", resolvedLang)
        }
        if (!request.headers.contains("X-App-Theme")) {
            val theme = settingsRepository().getCurrentTheme()
            val resolvedTheme = when (theme) {
                AppTheme.SYSTEM -> if (isSystemDarkTheme) AppTheme.DARK else AppTheme.LIGHT
                AppTheme.DARK -> AppTheme.DARK
                AppTheme.LIGHT -> AppTheme.LIGHT
            }
            request.headers.append("X-App-Theme", resolvedTheme.name)
        }
    }
}

private fun AppLanguage.toLanguageCode(): String {
    return when (this) {
        AppLanguage.ENGLISH -> "EN"
        AppLanguage.ARABIC -> "AR"
        AppLanguage.DEFAULT -> if (languageCode.lowercase().contains("ar")) "AR" else "EN"
    }
}
