package com.teEcclesia.identity.presentation.screen.attendance.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.attendance.UserAttendanceHistory
import com.teEcclesia.shared.domain.utils.formatTime
import com.teEcclesia.shared.domain.utils.getNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.attended_at
import teecclesia.designsystem.generated.resources.ic_calendar
import teecclesia.designsystem.generated.resources.ic_clock

@Composable
fun AttendanceHistoryCard(
    history: UserAttendanceHistory,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Theme.colorScheme.surfaceContainerHighest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = history.serviceName,
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Theme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${stringResource(Res.string.attended_at)}: ${history.registeredAt.time.formatTime()}",
                        style = Theme.typography.labelSmall,
                        color = Theme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            val eventName = history.eventName
            if (!eventName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = eventName,
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_calendar),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = history.eventDate.toString(),
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_clock),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${history.startTime.formatTime()} - ${history.endTime.formatTime()}",
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AttendanceHistoryCardPreview() = Theme {
    AttendanceHistoryCard(
        history = UserAttendanceHistory(
            id = 1,
            eventId = 10,
            serviceId = 2,
            serviceName = "اجتماع ثانوي",
            eventName = "درس أسبوعي",
            eventDate = LocalDate(2026, 9, 12),
            startTime = LocalTime(18, 0),
            endTime = LocalTime(20, 0),
            registeredAt = getNow()
        )
    )
}
