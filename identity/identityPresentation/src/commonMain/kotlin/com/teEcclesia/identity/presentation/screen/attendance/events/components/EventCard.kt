package com.teEcclesia.identity.presentation.screen.attendance.events.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.teEcclesia.identity.domain.model.attendance.ServiceEvent
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.allowed_period
import teecclesia.designsystem.generated.resources.attendance
import teecclesia.designsystem.generated.resources.ic_arrow_right
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_profile
import teecclesia.designsystem.generated.resources.ic_repeat
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.repeated

@Composable
fun EventCard(
    event: ServiceEvent,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isResponsible: Boolean = true
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                val eventName = event.name
                if (!eventName.isNullOrBlank()) {
                    Text(
                        text = eventName,
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = event.eventDate.toString(),
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${stringResource(Res.string.allowed_period)}: ${event.startTime} - ${event.endTime}",
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))


                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Theme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_profile),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Theme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${stringResource(Res.string.attendance)}: ${event.attendeeCount}",
                                style = Theme.typography.labelSmall,
                                color = Theme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    if (event.repeated) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Theme.colorScheme.primaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_repeat),
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Theme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = stringResource(Res.string.repeated),
                                    style = Theme.typography.labelSmall,
                                    color = Theme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isResponsible) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_user_settings),
                            contentDescription = "Edit event",
                            tint = Theme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Delete event",
                            tint = Theme.colorScheme.error
                        )
                    }
                }

                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = "Open event",
                    tint = Theme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun EventCardPreview() = Theme {
    EventCard(
        event = ServiceEvent(
            id = 1,
            serviceId = 1,
            name = "اجتماع الجمعة",
            eventDate = LocalDate.parse("2026-07-30"),
            startTime = LocalTime(18, 0),
            endTime = LocalTime(20, 0),
            attendeeCount = 25,
            true,
            createdAt = getNow()
        ),
        onClick = {},
        onEdit = {},
        onDelete = {}
    )
}
