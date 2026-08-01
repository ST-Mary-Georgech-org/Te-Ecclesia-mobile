package com.teEcclesia.identity.presentation.screen.attendance.register

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.TextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.identity.domain.model.attendance.EventAttendee
import com.teEcclesia.shared.domain.utils.getNow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_person
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm
import teecclesia.designsystem.generated.resources.confirm_remove_attendee
import teecclesia.designsystem.generated.resources.delete
import teecclesia.designsystem.generated.resources.enter_user_code
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_search
import teecclesia.designsystem.generated.resources.registered_people
import teecclesia.designsystem.generated.resources.role_admin
import teecclesia.designsystem.generated.resources.role_khadem
import teecclesia.designsystem.generated.resources.role_makhdoom
import teecclesia.designsystem.generated.resources.role_parent
import teecclesia.designsystem.generated.resources.role_priest

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
                .padding(horizontal = 24.dp, vertical = 16.dp)
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

                    Text(
                        text = state.eventName.ifBlank { stringResource(Res.string.registered_people) },
                        style = Theme.typography.headlineSmall,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                IconButton(onClick = listener::onClickAddPerson) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_plus),
                        contentDescription = "Add person",
                        tint = Theme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
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

        BottomSheet(
            isVisible = state.isAddPersonSheetOpen,
            onDismiss = listener::onDismissSheet
        ) {
            Text(
                text = stringResource(Res.string.add_person),
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.userCodeInput,
                    onValueChange = listener::onUserCodeChanged,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.enter_user_code),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = listener::onSearchUserByCode) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Theme.colorScheme.primary
                    )
                }
            }

            if (state.isSearchingUser) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            }

            state.searchUserError?.let { error ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = error,
                    style = Theme.typography.bodyMedium,
                    color = Theme.colorScheme.error
                )
            }

            state.searchedUser?.let { user ->
                Spacer(modifier = Modifier.height(16.dp))
                UserPreviewCard(user = user)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onConfirmAddPerson,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.confirm),
                state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled
            )
        }

        BottomSheet(
            isVisible = state.isRemoveConfirmSheetOpen,
            onDismiss = listener::onDismissSheet
        ) {
            Text(
                text = stringResource(Res.string.confirm_remove_attendee),
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    type = AppButtonType.Secondary,
                    onClick = listener::onDismissSheet,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.cancel)
                )

                AppButton(
                    type = AppButtonType.Primary,
                    onClick = listener::onConfirmRemoveAttendee,
                    modifier = Modifier.weight(1f),
                    text = stringResource(Res.string.delete),
                    state = if (state.isActionLoading) AppButtonState.Loading else AppButtonState.Enabled,
                    enablePrimaryBackgroundColor = Theme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AttendeeCard(
    attendee: EventAttendee,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
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
                        text = "(${attendee.role.toDisplayString()})",
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

@Composable
private fun UserPreviewCard(
    user: AttendeeUserPreview,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Theme.colorScheme.secondaryContainer
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = user.name,
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = user.role.toDisplayString(),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.primary
            )

            val stageAndYear = listOfNotNull(user.stageName, user.yearName).joinToString(" - ")
            if (stageAndYear.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stageAndYear,
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun UserRole.toDisplayString(): String {
    return when (this) {
        UserRole.MAKHDOOM -> stringResource(Res.string.role_makhdoom)
        UserRole.KHADEM -> stringResource(Res.string.role_khadem)
        UserRole.PARENT -> stringResource(Res.string.role_parent)
        UserRole.KAHEN -> stringResource(Res.string.role_priest)
        UserRole.ADMIN -> stringResource(Res.string.role_admin)
        UserRole.GUEST -> "ضيف"
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
            override fun onClickAddPerson() {}
            override fun onUserCodeChanged(code: String) {}
            override fun onSearchUserByCode() {}
            override fun onConfirmAddPerson() {}
            override fun onClickRemoveAttendee(attendee: EventAttendee) {}
            override fun onConfirmRemoveAttendee() {}
            override fun onDismissSheet() {}
        }
    )
}
