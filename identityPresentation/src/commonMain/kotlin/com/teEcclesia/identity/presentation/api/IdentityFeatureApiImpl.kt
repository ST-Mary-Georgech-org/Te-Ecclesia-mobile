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
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyPhoneRoute
import com.teEcclesia.identity.presentation.screen.login.LoginScreen
import com.teEcclesia.identity.presentation.screen.signup.SignUpScreen
import com.teEcclesia.identity.presentation.screen.verifyPhone.VerifyPhoneScreen
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.profile

class IdentityFeatureApiImpl : IdentityFeatureApi {

    override fun invoke(): (NavKey) -> NavEntry<NavKey> {
        return entryProvider {
            entry<LoginRoute> { LoginScreen() }
            entry<SignUpRoute> { SignUpScreen() }
            entry<VerifyPhoneRoute> { route ->
                VerifyPhoneScreen(
                    phone = route.phone,
                    isForgetPasswordFlow = route.isForgetPasswordFlow
                )
            }
            entry<ProfileRoute> {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(
        Modifier.fillMaxSize().background(Color.Blue),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column {
            Text(Res.string.profile.asString(), Theme.typography.label.medium.medium)
        }
    }
}
