package com.teEcclesia

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.appEntryPoint.EntryPoint
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.navigation.effector.Effector
import com.teEcclesia.identity.domain.repository.AuthenticationRepository
import com.teEcclesia.identity.domain.repository.SettingsRepository
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppLocalizer
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.util.SetSystemBarsAppearance
import org.koin.compose.koinInject


@Preview
@Composable
fun App(
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    settingsRepository: SettingsRepository = koinInject(),
    appLocalizer: AppLocalizer = koinInject(),
    authenticationRepository: AuthenticationRepository = koinInject(),
    effector: Effector = koinInject()
) {
    val currentTheme by settingsRepository.observeAppTheme().collectAsStateWithLifecycle()
    val currentLanguage by settingsRepository.observeAppLanguage().collectAsStateWithLifecycle()

    val isDarkTheme = when (currentTheme) {
        AppTheme.SYSTEM -> isSystemDarkTheme
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }

    val languageCode = if (currentLanguage == AppLanguage.DEFAULT) {
        appLocalizer.getDeviceLanguageIso()
    } else {
        currentLanguage.iso
    }

    Theme(
        language = languageCode,
        darkTheme = isDarkTheme,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                SetSystemBarsAppearance(currentTheme)
                EntryPoint()
            }
        }
    )
}
