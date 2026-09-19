package com.teEcclesia.identity.presentation.screen.reviewDeletionRequest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.chips.SuggestionChip
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.preview.PreviewThemes
import com.teEcclesia.identity.api.ReviewDeletionRequestRoute
import com.teEcclesia.identity.presentation.screen.requests.toText
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.approve_deletion
import teecclesia.designsystem.generated.resources.deletion_reason
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_clock
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.reject_deletion
import teecclesia.designsystem.generated.resources.requested_at
import teecclesia.designsystem.generated.resources.review_deletion_request

@Composable
fun ReviewDeletionRequestScreen(
    route: ReviewDeletionRequestRoute,
    viewModel: ReviewDeletionRequestViewModel = koinViewModel { parametersOf(route) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReviewDeletionRequestContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun ReviewDeletionRequestContent(
    state: ReviewDeletionRequestUiState,
    listener: ReviewDeletionRequestInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Theme.colorScheme.onBackground,
                modifier = Modifier.clickableNoRipple { listener.onClickBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(Res.string.review_deletion_request),
                style = Theme.typography.titleLarge,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Theme.colorScheme.surfaceContainerHighest,
                border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.userImageUrl.isNullOrBlank()) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Theme.colorScheme.secondaryContainer)
                                .border(1.dp, Theme.colorScheme.outlineVariant, CircleShape)
                                .padding(12.dp)
                        )
                    } else {
                        AsyncImage(
                            model = state.userImageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Theme.colorScheme.outlineVariant)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = state.userName,
                            style = Theme.typography.titleMedium,
                            color = Theme.colorScheme.onBackground
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.userRole != null) {
                                SuggestionChip(
                                    label = {
                                        Text(
                                            text = stringResource(state.userRole.toText()),
                                            style = Theme.typography.labelMedium,
                                            color = Theme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            }
                            if (!state.userCode.isNullOrBlank()) {
                                SuggestionChip(
                                    label = {
                                        Text(
                                            text = state.userCode,
                                            style = Theme.typography.labelMedium,
                                            color = Theme.colorScheme.primary
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (state.requestedAt.isNotBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Theme.colorScheme.surfaceContainerHighest,
                    border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clock),
                            contentDescription = null,
                            tint = Theme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(Res.string.requested_at),
                                style = Theme.typography.labelMedium,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = state.requestedAt,
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Theme.colorScheme.surfaceContainerHighest,
                border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.deletion_reason),
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onBackground
                    )

                    Text(
                        text = state.reason.ifBlank { "—" },
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onRejectDeletion,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.reject_deletion),
                state = state.rejectButtonState
            )

            AppButton(
                type = AppButtonType.Secondary,
                onClick = listener::onApproveDeletion,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.approve_deletion),
                state = state.approveButtonState,
                enableSecondaryBackgroundColor = Theme.colorScheme.error.copy(alpha = 0.12f)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ReviewDeletionRequestContentPreview() = Theme {
    ReviewDeletionRequestContent(
        state = ReviewDeletionRequestUiState(
            requestId = "1",
            userId = "usr-1",
            userName = "مينا يسي كامل",
            userCode = "M12345678",
            userImageUrl = null,
            userRole = UserRole.MAKHDOOM,
            reason = "لم أعد أستطيع حضور الاجتماعات في الوقت الحالي نظراً لظروف العمل",
            requestedAt = "2026-09-16 14:30"
        ),
        listener = object : ReviewDeletionRequestInteractionListener {
            override fun onApproveDeletion() {}
            override fun onRejectDeletion() {}
            override fun onClickBack() {}
        }
    )
}
