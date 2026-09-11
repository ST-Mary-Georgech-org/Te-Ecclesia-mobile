package com.teEcclesia.identity.presentation.screen.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.navigation.BackHandler
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.util.AppLanguage
import com.teEcclesia.identity.domain.util.AppTheme
import com.teEcclesia.identity.presentation.screen.login.components.LoginFormContent
import com.teEcclesia.identity.presentation.screen.login.components.OnboardingContent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.hello_welcome_back
import teecclesia.designsystem.generated.resources.logo

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.checkNotificationPermission()
        }
    }

    LoginScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
fun LoginScreenContent(
    state: LoginScreenState,
    interactionListener: LoginInteractionListener
) {
    BackHandler {
        interactionListener.onBackPressed()
    }

    LazyColumn(
        Modifier.fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.hello_welcome_back),
                    style = Theme.typography.headlineLarge,
                    color = Theme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        item {
            AnimatedContent(
                targetState = state.isOnboarding,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "LoginTransition"
            ) { isOnboarding ->
                if (isOnboarding) {
                    OnboardingContent(state, interactionListener)
                } else {
                    LoginFormContent(state, interactionListener)
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() = Theme {
    LoginScreenContent(
        state = LoginScreenState(
            username = "",
            password = "",
            usernameError = null,
            passwordError = null,
            isPasswordVisible = false,
            actionButtonState = AppButtonState.Enabled,
            isOnboarding = true,
            selectedLanguage = AppLanguage.ENGLISH,
        ),
        interactionListener = object : LoginInteractionListener {
            override fun onLoginClicked() {}
            override fun onSignUpClicked() {}
            override fun onForgotPasswordClicked() {}
            override fun onUsernameChange(newUsername: String) {}
            override fun onPasswordChange(newPassword: String) {}
            override fun onTogglePasswordVisibility() {}
            override fun onLanguageSelected(language: AppLanguage) {}
            override fun onThemeSelected(theme: AppTheme) {}
            override fun onContinueClicked() {}
            override fun onEnableNotificationsClicked() {}
            override fun onBackPressed() {}
        }
    )
}

