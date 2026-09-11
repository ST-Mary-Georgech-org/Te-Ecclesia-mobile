package com.teEcclesia.identity.presentation.screen.attendance.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.presentation.screen.attendance.register.toDisplayString
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.getNow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_close

@Composable
fun AttendeeCard(
    attendee: EventAttendee,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    isResponsible: Boolean = true
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Theme.colorScheme.surfaceContainerHighest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = attendee.name,
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.padding(start = 8.dp))

                    Text(
                        text = "(${stringResource(attendee.role.toDisplayString())})",
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.primary
                    )
                }

                val stageAndYear = listOfNotNull(attendee.stageName, attendee.yearName).joinToString(" - ")
                if (stageAndYear.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stageAndYear,
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isResponsible) {
                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "Remove attendee",
                        tint = Theme.colorScheme.error
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AttendeeCardPreview() = Theme {
    AttendeeCard(
        attendee = EventAttendee(
            id = 1,
            eventId = 1,
            userId = "u1",
            name = "ماريو عماد",
            role = UserRole.MAKHDOOM,
            stageName = "ابتدائي",
            yearName = "السادسة",
            registeredAt = getNow()
        ),
        onRemove = {}
    )
}
