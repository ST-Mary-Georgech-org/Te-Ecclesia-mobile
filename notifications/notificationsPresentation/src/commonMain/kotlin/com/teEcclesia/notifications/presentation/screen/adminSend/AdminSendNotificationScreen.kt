package com.teEcclesia.notifications.presentation.screen.adminSend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.button.AppSegmentedControl
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.icon.IconButton
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.sheet.EducationalStageSelectField
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.domain.model.attendance.AttendeeUserPreview
import com.teEcclesia.lookups.domain.model.LookupResponse
import com.teEcclesia.shared.domain.model.UserRole
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.add_payload_field
import teecclesia.designsystem.generated.resources.all_roles
import teecclesia.designsystem.generated.resources.custom_payload
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ic_close
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.ic_profile_image_placeholder
import teecclesia.designsystem.generated.resources.ic_search
import teecclesia.designsystem.generated.resources.ic_success
import teecclesia.designsystem.generated.resources.notification_body
import teecclesia.designsystem.generated.resources.notification_body_hint
import teecclesia.designsystem.generated.resources.notification_sent_successfully
import teecclesia.designsystem.generated.resources.notification_title
import teecclesia.designsystem.generated.resources.notification_title_hint
import teecclesia.designsystem.generated.resources.payload_key
import teecclesia.designsystem.generated.resources.payload_value
import teecclesia.designsystem.generated.resources.search_users_hint
import teecclesia.designsystem.generated.resources.select_role
import teecclesia.designsystem.generated.resources.send
import teecclesia.designsystem.generated.resources.send_notification
import teecclesia.designsystem.generated.resources.target_recipients

@Composable
fun AdminSendNotificationScreen(
    viewModel: AdminSendNotificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AdminSendNotificationContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun AdminSendNotificationContent(
    state: AdminSendNotificationUiState,
    listener: AdminSendNotificationInteractionListener,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = listener::onClickBack) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Theme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.send_notification),
                style = Theme.typography.headlineSmall,
                color = Theme.colorScheme.onBackground
            )
        }

        HorizontalDivider(color = Theme.colorScheme.outlineVariant)

        AnimatedVisibility(
            visible = state.isSuccessBannerVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            SuccessBanner(
                onDismiss = listener::onDismissSuccessBanner,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(Res.string.target_recipients),
                    style = Theme.typography.titleMedium,
                    color = Theme.colorScheme.onBackground
                )

                AppSegmentedControl(
                    options = listOf(RecipientTargetMode.SPECIFIC_USERS, RecipientTargetMode.TARGET_GROUP),
                    selectedOption = state.targetMode,
                    onOptionSelected = listener::onTargetModeSelected,
                    getName = { toDisplayTitle() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (state.targetMode == RecipientTargetMode.SPECIFIC_USERS) {
                SpecificUsersSelector(
                    state = state,
                    listener = listener
                )
            } else {
                TargetGroupSelector(
                    state = state,
                    listener = listener
                )
            }

            HorizontalDivider(color = Theme.colorScheme.outlineVariant)

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                CustomTextField(
                    value = state.title,
                    onValueChange = listener::onTitleChanged,
                    labelText = stringResource(Res.string.notification_title),
                    errorText = state.titleError?.asString(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    allowEmojis = true
                )

                CustomTextField(
                    value = state.body,
                    onValueChange = listener::onBodyChanged,
                    labelText = stringResource(Res.string.notification_body),
                    errorText = state.bodyError?.asString(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 3,
                    maxLines = 6,
                    allowEmojis = true
                )
            }

            HorizontalDivider(color = Theme.colorScheme.outlineVariant)

            PayloadSection(
                state = state,
                listener = listener
            )

            Spacer(modifier = Modifier.height(10.dp))

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onClickSend,
                text = stringResource(Res.string.send),
                state = state.actionButtonState,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SpecificUsersSelector(
    state: AdminSendNotificationUiState,
    listener: AdminSendNotificationInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CustomTextField(
                value = state.userSearchQuery,
                onValueChange = listener::onUserSearchQueryChanged,
                labelText = stringResource(Res.string.search_users_hint),
                trailingIcon = painterResource(Res.drawable.ic_search),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            DropdownMenu(
                expanded = state.isSuggestionsDropdownVisible && (state.suggestedUsers.isNotEmpty() || state.isSearchingSuggestions),
                onDismissRequest = listener::onDismissSuggestionsDropdown,
                properties = PopupProperties(focusable = false),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                if (state.isSearchingSuggestions && state.suggestedUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Theme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    state.suggestedUsers.forEach { user ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (user.imageUrl.isNullOrBlank()) {
                                        Icon(
                                            painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                                            contentDescription = null,
                                            tint = Theme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Theme.colorScheme.secondaryContainer)
                                                .padding(6.dp)
                                        )
                                    } else {
                                        AsyncImage(
                                            model = user.imageUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = user.name,
                                            style = Theme.typography.titleMedium,
                                            color = Theme.colorScheme.onSurface
                                        )
                                        user.code?.let { code ->
                                            if (code.isNotBlank()) {
                                                Text(
                                                    text = code,
                                                    style = Theme.typography.labelSmall,
                                                    color = Theme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            onClick = { listener.onSelectSuggestedUser(user) }
                        )
                    }
                }
            }
        }

        if (state.selectedUsers.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                state.selectedUsers.forEach { user ->
                    SelectedUserChip(
                        user = user,
                        onRemove = { listener.onRemoveSelectedUser(user) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedUserChip(
    user: AttendeeUserPreview,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Theme.colorScheme.surfaceContainerHighest,
        border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (!user.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = user.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_profile_image_placeholder),
                        contentDescription = null,
                        tint = Theme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Theme.colorScheme.secondaryContainer)
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = user.name,
                        style = Theme.typography.bodyLarge,
                        color = Theme.colorScheme.onSurface
                    )
                    user.code?.let { code ->
                        if (code.isNotBlank()) {
                            Text(
                                text = code,
                                style = Theme.typography.labelSmall,
                                color = Theme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(Res.drawable.ic_close),
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun TargetGroupSelector(
    state: AdminSendNotificationUiState,
    listener: AdminSendNotificationInteractionListener,
    modifier: Modifier = Modifier
) {
    val rolesList = listOf(
        null,
        UserRole.KHADEM,
        UserRole.MAKHDOOM,
        UserRole.PARENT,
        UserRole.ADMIN,
        UserRole.KAHEN,
        UserRole.GUEST
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(Res.string.select_role),
                style = Theme.typography.labelMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { listener.onToggleRoleDropdown() },
                    shape = RoundedCornerShape(16.dp),
                    color = Theme.colorScheme.surfaceContainerHighest,
                    border = BorderStroke(1.dp, Theme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(state.selectedRole.toRoleDisplayTitle()),
                            style = Theme.typography.bodyLarge,
                            color = Theme.colorScheme.onSurface
                        )
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = Theme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                DropdownMenu(
                    expanded = state.isRoleDropdownExpanded,
                    onDismissRequest = listener::onDismissRoleDropdown,
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    rolesList.forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(role.toRoleDisplayTitle()),
                                    style = Theme.typography.bodyMedium,
                                    color = Theme.colorScheme.onSurface
                                )
                            },
                            onClick = { listener.onSelectRole(role) }
                        )
                    }
                }
            }
        }

        if (state.isEducationalStageVisible) {
            EducationalStageSelectField(
                selectedStage = state.selectedEducationalStage,
                educationalStages = state.educationalStages,
                itemTitle = { it.name },
                itemId = { it.id },
                isSelected = { it.id == state.selectedEducationalStage?.id },
                isSheetVisible = state.isStageSheetVisible,
                onToggleSheet = listener::onToggleStageSheet,
                onSelectStage = listener::onSelectStage,
                allowClear = true,
                onLoadNextStages = listener::onLoadNextStages,
                isStageLoading = state.isLoadingStages,
                isStageLoadFailed = state.isStageLoadFailed,
                onRetryLoadStages = listener::onRetryLoadStages,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PayloadSection(
    state: AdminSendNotificationUiState,
    listener: AdminSendNotificationInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableNoRipple { listener.onTogglePayloadExpanded() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.custom_payload),
                style = Theme.typography.titleMedium,
                color = Theme.colorScheme.onBackground
            )

            IconButton(onClick = listener::onAddPayloadItem) {
                Icon(
                    painter = painterResource(Res.drawable.ic_plus),
                    contentDescription = null,
                    tint = Theme.colorScheme.primary
                )
            }
        }

        AnimatedVisibility(
            visible = state.isPayloadExpanded && state.payloadItems.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.payloadItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomTextField(
                            value = item.key,
                            onValueChange = { listener.onPayloadKeyChanged(item.id, it) },
                            labelText = stringResource(Res.string.payload_key),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        CustomTextField(
                            value = item.value,
                            onValueChange = { listener.onPayloadValueChanged(item.id, it) },
                            labelText = stringResource(Res.string.payload_value),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        IconButton(onClick = { listener.onRemovePayloadItem(item.id) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_close),
                                contentDescription = null,
                                tint = Theme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessBanner(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = Theme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_success),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(Res.string.notification_sent_successfully),
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Theme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun AdminSendNotificationContentPreview() = Theme {
    AdminSendNotificationContent(
        state = AdminSendNotificationUiState(isSuccessBannerVisible = true),
        listener = object : AdminSendNotificationInteractionListener {
            override fun onClickBack() {}
            override fun onTargetModeSelected(mode: RecipientTargetMode) {}
            override fun onTitleChanged(title: String) {}
            override fun onBodyChanged(body: String) {}
            override fun onUserSearchQueryChanged(query: String) {}
            override fun onSelectSuggestedUser(user: AttendeeUserPreview) {}
            override fun onRemoveSelectedUser(user: AttendeeUserPreview) {}
            override fun onDismissSuggestionsDropdown() {}
            override fun onToggleRoleDropdown() {}
            override fun onSelectRole(role: UserRole?) {}
            override fun onDismissRoleDropdown() {}
            override fun onToggleStageSheet(isVisible: Boolean) {}
            override fun onSelectStage(stage: LookupResponse?) {}
            override fun onLoadNextStages() {}
            override fun onRetryLoadStages() {}
            override fun onTogglePayloadExpanded() {}
            override fun onAddPayloadItem() {}
            override fun onPayloadKeyChanged(id: String, key: String) {}
            override fun onPayloadValueChanged(id: String, value: String) {}
            override fun onRemovePayloadItem(id: String) {}
            override fun onClickSend() {}
            override fun onDismissSuccessBanner() {}
        }
    )
}
