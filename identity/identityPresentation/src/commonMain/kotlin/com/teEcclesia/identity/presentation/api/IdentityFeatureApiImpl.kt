package com.teEcclesia.identity.presentation.api

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.presentation.screen.login.LoginScreen

import com.teEcclesia.identity.api.SplashRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.RegistrationRequestsRoute
import com.teEcclesia.identity.presentation.screen.splash.SplashScreen
import com.teEcclesia.identity.presentation.screen.register.RegisterScreen
import com.teEcclesia.identity.presentation.screen.requests.RegistrationRequestsScreen

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
            entry<RegistrationRequestsRoute> { RegistrationRequestsScreen() }
        }
    }
}
