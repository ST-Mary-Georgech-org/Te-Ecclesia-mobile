package com.teEcclesia.identity.presentation.screen.attendance.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.scanner.EmbeddedBarcodeScannerCard
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.identity.presentation.screen.attendance.register.components.AttendeeCard
import com.teEcclesia.identity.presentation.screen.attendance.register.components.MemberCodeInputField
import com.teEcclesia.identity.presentation.screen.attendance.register.components.RemoveAttendeeConfirmSheet
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.shared.domain.utils.getNow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_error
import teecclesia.designsystem.generated.resources.no_registered_attendees
import teecclesia.designsystem.generated.resources.registered_people

@Composable
fun AttendanceRegisterScreen(
    eventId: Long,
    serviceName: String,
    eventName: String,
    viewModel: AttendanceRegisterViewModel = koinViewModel(parameters = { parametersOf(eventId, serviceName, eventName) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AttendanceRegisterContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun AttendanceRegisterContent(
    state: AttendanceRegisterUiState,
    listener: AttendanceRegisterInteractionListener,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = listener::onClickBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Theme.colorScheme.onBackground
                        )
                    }

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = state.eventName.ifBlank { stringResource(Res.string.registered_people) },
                            style = Theme.typography.headlineSmall,
                            color = Theme.colorScheme.onBackground
                        )
                        if (state.serviceName.isNotBlank()) {
                            Text(
                                text = state.serviceName,
                                style = Theme.typography.bodySmall,
                                color = Theme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Theme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${state.attendees.size}",
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            MemberCodeInputField(
                code = state.userCodeInput,
                onCodeChange = listener::onUserCodeChanged,
                onOpenScanner = listener::onToggleScanner,
                onManualSubmit = listener::onManualSubmit,
                isScannerOpen = state.isScannerOpen
            )

            EmbeddedBarcodeScannerCard(
                isVisible = state.isScannerOpen,
                onClose = listener::onCloseScanner,
                onQrCodeScanned = listener::onQrCodeScanned,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            AnimatedVisibility(
                visible = state.searchUserError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.searchUserError?.let { errorText ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Theme.colorScheme.errorContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_error),
                                contentDescription = null,
                                tint = Theme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = errorText.asString(),
                                style = Theme.typography.bodySmall,
                                color = Theme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            if (state.isActionLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Theme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else if (state.attendees.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_registered_attendees),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(state.attendees, key = { it.id }) { attendee ->
                        AttendeeCard(
                            attendee = attendee,
                            onRemove = { listener.onClickRemoveAttendee(attendee) }
                        )
                    }
                }
            }
        }

        RemoveAttendeeConfirmSheet(
            state = state,
            listener = listener
        )
    }
}

@PreviewLightDark
@Composable
private fun AttendanceRegisterPreview() = Theme {
    AttendanceRegisterContent(
        state = AttendanceRegisterUiState(
            eventName = "اجتماع الجمعة",
            attendees = listOf(
                EventAttendee(1, 1, "u1", "ماريو عماد", UserRole.MAKHDOOM, "ابتدائي", "السادسة", getNow())
            )
        ),
        listener = object : AttendanceRegisterInteractionListener {
            override fun onClickBack() {}
            override fun onToggleScanner() {}
            override fun onCloseScanner() {}
            override fun onUserCodeChanged(code: String) {}
            override fun onManualSubmit() {}
            override fun onQrCodeScanned(code: String) {}
            override fun onClickRemoveAttendee(attendee: EventAttendee) {}
            override fun onConfirmRemoveAttendee() {}
            override fun onDismissSheet() {}
        }
    )
}
