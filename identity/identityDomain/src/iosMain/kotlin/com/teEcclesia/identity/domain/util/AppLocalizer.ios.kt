package com.teEcclesia.identity.domain.util

import com.teEcclesia.identity.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual class AppLocalizer(
    private val settingsRepository: SettingsRepository
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        coroutineScope.launch {
            settingsRepository.observeAppLanguage().collectLatest { currentLanguage ->
                val iso = if (currentLanguage == AppLanguage.DEFAULT) {
                    getDeviceLanguageIso()
                } else {
                    currentLanguage.iso
                }
                val defaults = NSUserDefaults.standardUserDefaults
                defaults.setObject(arrayListOf(iso), forKey = "AppleLanguages")
            }
        }
    }

    actual fun getDeviceLanguageIso(): String {
        val deviceIso = NSLocale.preferredLanguages.firstOrNull()?.toString()?.split("-")
            ?.firstOrNull() ?: AppLanguage.ENGLISH.iso
        return deviceIso
    }
}
