package com.teEcclesia.appEntryPoint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.snackbar.AnimatedSnackBar
import com.teEcclesia.identity.api.IdentityFeatureApi
import com.teEcclesia.identity.domain.service.AuthorizationService
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EntryPoint(
    identityApi: IdentityFeatureApi = koinInject(),
    viewModel: MainEntryViewModel = koinViewModel(),
    authorizationService: AuthorizationService = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val accessToken by authorizationService.observeAccessToken().collectAsStateWithLifecycle()
    var previousAccessTokenWasBlank by remember { mutableStateOf(accessToken.isBlank()) }

    LaunchedEffect(accessToken) {
        if (previousAccessTokenWasBlank && accessToken.isNotBlank()) {
            viewModel.onBottomNavigationChanged(true)
        }
        previousAccessTokenWasBlank = accessToken.isBlank()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AnimatedSnackBar(
            isVisible = state.isSnackBarVisible,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1000f)
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 12.dp, end = 16.dp),
            onDismiss = viewModel::hideSnackBar,
            data = state.snackBarData
        )

        if (accessToken.isBlank()) {
            identityApi.LoginFlow(
                updateBottomNavigationVisibility = viewModel::onBottomNavigationChanged,
                showSnackBar = viewModel::showSnackBar
            )
            return
        }

        LoggedInContainer(
            state = state,
            listener = viewModel,
        )
    }
}