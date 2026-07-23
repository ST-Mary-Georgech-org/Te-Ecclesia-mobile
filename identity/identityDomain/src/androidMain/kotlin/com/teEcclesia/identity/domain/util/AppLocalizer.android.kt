package com.teEcclesia.identity.domain.util
import android.content.Context
import android.os.LocaleList
import androidx.core.os.LocaleListCompat
import com.teEcclesia.identity.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

actual class AppLocalizer(
    private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentLanguage: String = AppLanguage.ENGLISH.iso

    init {
        coroutineScope.launch {
            settingsLanguageFlow()
        }
    }

    fun applyLocaleToContext(iso: String = currentLanguage) {
        val localeList = LocaleList.forLanguageTags(iso)
        LocaleList.setDefault(localeList)
        val config = context.resources.configuration
        config.setLocales(localeList)
    }

    private suspend fun settingsLanguageFlow() {
        settingsRepository.observeAppLanguage().collect { lang ->
            val iso = if (lang == AppLanguage.DEFAULT) {
                getDeviceLanguageIso()
            } else {
                lang.iso
            }
            currentLanguage = iso
            applyLocaleToContext(currentLanguage)
        }
    }

    actual fun getDeviceLanguageIso(): String {
        val deviceLocale = LocaleListCompat.getDefault()[0]
        return deviceLocale?.language ?: AppLanguage.ENGLISH.iso
    }
}
