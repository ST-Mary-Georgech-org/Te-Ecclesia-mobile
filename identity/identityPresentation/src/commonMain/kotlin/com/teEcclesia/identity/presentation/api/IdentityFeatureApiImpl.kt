package com.teEcclesia.identity.presentation.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.presentation.screen.login.LoginScreen
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.profile

import com.teEcclesia.identity.api.SplashRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.presentation.screen.splash.SplashScreen
import com.teEcclesia.identity.presentation.screen.register.RegisterScreen

import com.teEcclesia.identity.api.PendingApprovalRoute
import com.teEcclesia.identity.presentation.screen.pendingApproval.PendingApprovalScreen

import com.teEcclesia.identity.presentation.screen.profile.ProfileScreen

class IdentityFeatureApiImpl : IdentityFeatureApi {

    override fun invoke(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<SplashRoute> { SplashScreen() }
            entry<LoginRoute> { LoginScreen() }
            entry<SignUpRoute> { route -> RegisterScreen(isEditMode = route.isEditMode) }
            entry<PendingApprovalRoute> { PendingApprovalScreen() }
            entry<ProfileRoute> { ProfileScreen() }
        }
    }
}
