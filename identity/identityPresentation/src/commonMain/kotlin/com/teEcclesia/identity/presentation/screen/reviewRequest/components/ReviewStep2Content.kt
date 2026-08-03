package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.presentation.screen.requests.toText
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.accept
import teecclesia.designsystem.generated.resources.add_new_user
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.ic_document
import teecclesia.designsystem.generated.resources.ic_user_settings
import teecclesia.designsystem.generated.resources.notes
import teecclesia.designsystem.generated.resources.previous_step
import teecclesia.designsystem.generated.resources.reason
import teecclesia.designsystem.generated.resources.reject
import teecclesia.designsystem.generated.resources.role
import teecclesia.designsystem.generated.resources.save_changes
import teecclesia.designsystem.generated.resources.submitted_at

@Composable
fun ReviewStep2Content(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
    onFileClickOrdinationCertificate: (() -> Unit)? = null,
    onFileClickIdentityCertificate: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isRoleEditable) {
            ReviewSectionCard(
                title = stringResource(Res.string.role),
                icon = painterResource(Res.drawable.ic_user_settings)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomTextField(
                        value = stringResource(state.selectedRole.toText()),
                        onValueChange = {},
                        labelText = stringResource(Res.string.role),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { listener.onToggleRoleSheet(true) },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = painterResource(Res.drawable.ic_chevron_down)
                    )

                    DropdownMenu(
                        expanded = state.isRoleSheetVisible,
                        onDismissRequest = { listener.onToggleRoleSheet(false) },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        state.roles.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(role.toText()),
                                        style = Theme.typography.bodyMedium,
                                        color = Theme.colorScheme.onBackground
                                    )
                                },
                                onClick = {
                                    listener.onRoleSelected(role)
                                    listener.onToggleRoleSheet(false)
                                }
                            )
                        }
                    }
                }
            }
        }

        AnimatedContent(
            targetState = state.selectedRole,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "RoleContentTransition"
        ) { role ->
            when (role) {
                UserRole.MAKHDOOM -> ReviewStep2StudentContent(
                    state = state,
                    listener = listener,
                    onFileClickOrdinationCertificate = onFileClickOrdinationCertificate,
                    onFileClickIdentityCertificate = onFileClickIdentityCertificate
                )
                UserRole.KHADEM -> ReviewStep2ServantContent(state = state, listener = listener)
                UserRole.PARENT -> ReviewStep2ParentContent(
                    state = state,
                    listener = listener,
                    onFileClickIdentityCertificate = onFileClickIdentityCertificate
                )
                UserRole.KAHEN -> ReviewStep2KahenContent(state = state, listener = listener)
                else -> ReviewStep2StudentContent(
                    state = state,
                    listener = listener,
                    onFileClickOrdinationCertificate = onFileClickOrdinationCertificate,
                    onFileClickIdentityCertificate = onFileClickIdentityCertificate
                )
            }
        }

        ReviewSectionCard(
            title = stringResource(Res.string.notes),
            icon = painterResource(Res.drawable.ic_document)
        ) {
            CustomTextField(
                value = state.notes,
                onValueChange = listener::onNotesChanged,
                labelText = stringResource(Res.string.reason),
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3,
                errorText = state.notesError?.asString()
            )
        }

        if (state.submittedAt.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${stringResource(Res.string.submitted_at)}  ${state.submittedAt}",
                    style = Theme.typography.bodySmall,
                    color = Theme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.isUpdateMode || state.canEditUser) {
                AppButton(
                    text = stringResource(
                        if (state.isUpdateMode) Res.string.save_changes
                        else if (state.userId.isNotBlank()) Res.string.accept 
                        else Res.string.add_new_user
                    ),
                    onClick = { listener.onApproveRequest() },
                    type = AppButtonType.Tertiary,
                    state = if (state.isSubmitting) AppButtonState.Loading else AppButtonState.Enabled,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    text = stringResource(Res.string.previous_step),
                    onClick = { listener.onPreviousStep() },
                    type = AppButtonType.Secondary,
                    modifier = Modifier.weight(1f)
                )
                if (state.userId.isNotBlank() && !state.isReadOnlyMode) {
                    AppButton(
                        text = stringResource(Res.string.reject),
                        onClick = { listener.onRejectRequest(state.notes) },
                        type = AppButtonType.Error,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
