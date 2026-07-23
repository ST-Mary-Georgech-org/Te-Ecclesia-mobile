package com.teEcclesia.identity.presentation.screen.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppSegmentedControl
import com.teEcclesia.designsystem.components.button.Button
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.identity.presentation.screen.login.LoginInteractionListener
import com.teEcclesia.identity.presentation.screen.login.LoginScreenState
import com.teEcclesia.identity.presentation.screen.login.getName
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.continue_text
import teecclesia.designsystem.generated.resources.select_language
import teecclesia.designsystem.generated.resources.select_theme


@Composable
fun OnboardingContent(
    state: LoginScreenState,
    interactionListener: LoginInteractionListener
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Res.string.select_language.asString(),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        AppSegmentedControl(
            options = listOf(AppLanguage.ENGLISH, AppLanguage.ARABIC),
            selectedOption = state.selectedLanguage,
            onOptionSelected = interactionListener::onLanguageSelected,
            getName = { this.getName() },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = Res.string.select_theme.asString(),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        AppSegmentedControl(
            options = listOf(AppTheme.LIGHT, AppTheme.DARK),
            selectedOption = if (Theme.isDarkTheme) AppTheme.DARK else AppTheme.LIGHT,
            onOptionSelected = interactionListener::onThemeSelected,
            getName = { this.getName() },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = interactionListener::onContinueClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            containerColor = Theme.colorScheme.primary,
            contentColor = Theme.colorScheme.onPrimary
        ) {
            Text(
                text = Res.string.continue_text.asString(),
                style = Theme.typography.labelLarge,
                color = Theme.colorScheme.onPrimary
            )
        }
    }
}
