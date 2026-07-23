package com.teEcclesia.identity.presentation.screen.pendingApproval

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.Button
import com.teEcclesia.designsystem.components.navigation.BackHandler
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.TeEcclesiaPreview
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.account_under_review
import teecclesia.designsystem.generated.resources.back_to_login
import teecclesia.designsystem.generated.resources.edit_registration
import teecclesia.designsystem.generated.resources.pending_approval_description

@Composable
fun PendingApprovalScreen(
    viewModel: PendingApprovalViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PendingApprovalContent(
        state = state,
        listener = viewModel
    )
}

@Composable
fun PendingApprovalContent(
    state: PendingApprovalScreenState,
    listener: PendingApprovalInteractionListener
) {
    BackHandler {
        // Prevent going back
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.account_under_review),
                style = Theme.typography.headlineLarge,
                color = Theme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.pending_approval_description),
                style = Theme.typography.bodyLarge,
                color = Theme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = listener::onClickEditRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Theme.colorScheme.primary,
                contentColor = Theme.colorScheme.onPrimary
            ) {
                Text(
                    text = stringResource(Res.string.edit_registration),
                    style = Theme.typography.labelLarge,
                    color = Theme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = listener::onClickLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = Theme.colorScheme.secondaryContainer,
                contentColor = Theme.colorScheme.onSecondaryContainer
            ) {
                Text(
                    text = stringResource(Res.string.back_to_login),
                    style = Theme.typography.labelLarge,
                    color = Theme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PendingApprovalScreenPreview() {
    val listener = object : PendingApprovalInteractionListener {
        override fun onClickLogout() {}
        override fun onClickEditRequest() {}
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        TeEcclesiaPreview(darkTheme = Theme.isDarkTheme) {
            PendingApprovalContent(
                state = PendingApprovalScreenState(),
                listener = listener
            )
        }
    }
}
