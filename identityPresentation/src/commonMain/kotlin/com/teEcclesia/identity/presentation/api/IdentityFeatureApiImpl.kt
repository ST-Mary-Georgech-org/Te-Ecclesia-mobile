package com.teEcclesia.identity.presentation.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.presentation.navigation.IdentityNavHost
import com.teEcclesia.identity.presentation.navigation.LoginRoute
import com.teEcclesia.identity.presentation.navigation.ProfileRoute

class IdentityFeatureApiImpl : IdentityFeatureApi {

    @Composable
    override fun TabEntry(
        updateBottomNavigationVisibility: (Boolean) -> Unit,
        showSnackBar: (String, String?, Boolean, Painter?, Long?, Color) -> Unit
    ) {
        IdentityNavHost(
            updateBottomNavigationVisibility = updateBottomNavigationVisibility,
            showSnackBar = showSnackBar,
            startDestination = ProfileRoute
        )
    }

    @Composable
    override fun LoginFlow(
        updateBottomNavigationVisibility: (Boolean) -> Unit,
        showSnackBar: (String, String?, Boolean, Painter?, Long?, Color) -> Unit
    ) {
        IdentityNavHost(
            updateBottomNavigationVisibility = updateBottomNavigationVisibility,
            showSnackBar = showSnackBar,
            startDestination = LoginRoute
        )
    }
}