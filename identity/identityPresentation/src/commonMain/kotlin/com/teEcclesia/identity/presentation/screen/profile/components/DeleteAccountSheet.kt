package com.teEcclesia.identity.presentation.screen.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.confirm_delete_account
import teecclesia.designsystem.generated.resources.delete_account_password_hint
import teecclesia.designsystem.generated.resources.delete_account_reason_hint
import teecclesia.designsystem.generated.resources.delete_account_title
import teecclesia.designsystem.generated.resources.delete_account_warning
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened

@Composable
fun DeleteAccountSheet(
    isVisible: Boolean,
    reason: String,
    password: String,
    isPasswordVisible: Boolean,
    buttonState: AppButtonState,
    onReasonChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        DeleteAccountSheetContent(
            reason = reason,
            password = password,
            isPasswordVisible = isPasswordVisible,
            buttonState = buttonState,
            onReasonChange = onReasonChange,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onConfirmDelete = onConfirmDelete,
            onDismiss = onDismiss,
            modifier = modifier
        )
    }
}

@Composable
fun DeleteAccountSheetContent(
    reason: String,
    password: String,
    isPasswordVisible: Boolean,
    buttonState: AppButtonState,
    onReasonChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.delete_account_title),
            style = Theme.typography.titleLarge,
            color = Theme.colorScheme.error
        )

        Text(
            text = stringResource(Res.string.delete_account_warning),
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurfaceVariant
        )

        CustomTextField(
            value = reason,
            onValueChange = onReasonChange,
            labelText = stringResource(Res.string.delete_account_reason_hint),
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        CustomTextField(
            value = password,
            onValueChange = onPasswordChange,
            labelText = stringResource(Res.string.delete_account_password_hint),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = if (isPasswordVisible) {
                Res.drawable.ic_eye_closed.painter()
            } else {
                Res.drawable.ic_eye_opened.painter()
            },
            onTrailingIconClick = onTogglePasswordVisibility
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                type = AppButtonType.Secondary,
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel)
            )

            AppButton(
                type = AppButtonType.Primary,
                onClick = onConfirmDelete,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.confirm_delete_account),
                state = buttonState,
                enablePrimaryBackgroundColor = Theme.colorScheme.error
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DeleteAccountSheetPreview() = Theme {
    DeleteAccountSheetContent(
        reason = "",
        password = "",
        isPasswordVisible = false,
        buttonState = AppButtonState.Enabled,
        onReasonChange = {},
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onConfirmDelete = {},
        onDismiss = {}
    )
}
