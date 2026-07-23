package com.teEcclesia.identity.presentation.screen.requests.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.asString
import com.teEcclesia.identity.domain.model.ProfileResponse
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.presentation.screen.requests.toText

@Composable
fun RegistrationRequestCard(
    profile: ProfileResponse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Theme.colorScheme.surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = profile.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.outline)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = profile.fullName,
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onBackground
                    )

                    RoleChip(role = profile.role)
                }

                val makhdoomProfile = profile.makhdoomProfile
                if (makhdoomProfile != null) {
                    if (makhdoomProfile.educationalStage != null) {
                        Text(
                            text = "${makhdoomProfile.educationalStage} - ${makhdoomProfile.educationalYear ?: ""}",
                            style = Theme.typography.bodySmall,
                            color = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    if (makhdoomProfile.shamamsaStudyStatus != null) {
                        Text(
                            text = makhdoomProfile.shamamsaStudyStatus.toText().asString(),
                            style = Theme.typography.bodySmall,
                            color = Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleChip(role: UserRole) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Theme.colorScheme.primaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = role.toText().asString(),
            style = Theme.typography.labelSmall,
            color = Theme.colorScheme.onPrimaryContainer
        )
    }
}
