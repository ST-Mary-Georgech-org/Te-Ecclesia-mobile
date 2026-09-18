package com.teEcclesia.identity.presentation.screen.deletionRequests.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.chips.SuggestionChip
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.format
import com.teEcclesia.identity.domain.model.AccountDeletionRequest
import com.teEcclesia.identity.presentation.screen.requests.toText
import com.teEcclesia.shared.domain.model.UserRole
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder

@Composable
fun DeletionRequestCard(
    request: AccountDeletionRequest,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colorScheme.surfaceContainerHighest)
            .clickableNoRipple { onClick() }
            .border(1.dp, Theme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (request.userImageUrl == null) {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.secondaryContainer)
                        .border(1.dp, Theme.colorScheme.outlineVariant, CircleShape)
                        .padding(10.dp)
                )
            } else {
                AsyncImage(
                    model = request.userImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.outlineVariant)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = request.userName,
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.onBackground
                )

                Text(
                    text = request.requestedAt.format(),
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.onSurfaceVariant
                )

                if (request.reason.isNotBlank()) {
                    Text(
                        text = request.reason,
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SuggestionChip(
                            label = {
                                Text(
                                    text = stringResource(request.userRole.toText()),
                                    style = Theme.typography.labelMedium,
                                    color = Theme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        request.userCode?.let { code ->
                            SuggestionChip(
                                label = {
                                    Text(
                                        text = code,
                                        style = Theme.typography.labelMedium,
                                        color = Theme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Theme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeletionRequestCardPreview() = Theme {
    DeletionRequestCard(
        request = AccountDeletionRequest(
            id = "req-1",
            userId = "usr-1",
            userName = "مينا يسي كامل",
            userCode = "M12345678",
            userImageUrl = null,
            userRole = UserRole.MAKHDOOM,
            reason = "لم أعد أستطيع حضور الاجتماعات في الوقت الحالي",
            requestedAt = LocalDateTime(2026, 9, 16, 14, 30)
        ),
        onClick = {}
    )
}
