package com.teEcclesia.identity.presentation.screen.login

import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.utils.UiText
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import org.jetbrains.compose.resources.StringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.arabic
import teecclesia.designsystem.generated.resources.dark_theme
import teecclesia.designsystem.generated.resources.english
import teecclesia.designsystem.generated.resources.light_theme
import teecclesia.designsystem.generated.resources.system_theme

data class LoginScreenState(
    val actionButtonState: AppButtonState = AppButtonState.Enabled,
    val username: String = "",
    val usernameError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val isOnboarding: Boolean = true,
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
)


fun AppLanguage.getName(): StringResource = when (this) {
    AppLanguage.ENGLISH -> Res.string.english
    AppLanguage.ARABIC -> Res.string.arabic
    AppLanguage.DEFAULT -> Res.string.english
}

fun AppTheme.getName(): StringResource = when (this) {
    AppTheme.LIGHT -> Res.string.light_theme
    AppTheme.DARK -> Res.string.dark_theme
    AppTheme.SYSTEM -> Res.string.system_theme
}
