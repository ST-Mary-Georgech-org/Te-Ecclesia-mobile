package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.util.extentions.painter
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.deleted_account_found
import teecclesia.designsystem.generated.resources.forget_your_password
import teecclesia.designsystem.generated.resources.ic_eye_closed
import teecclesia.designsystem.generated.resources.ic_eye_opened
import teecclesia.designsystem.generated.resources.password
import teecclesia.designsystem.generated.resources.reactivate_account
import teecclesia.designsystem.generated.resources.reactivate_account_prompt

@Composable
fun ReactivateAccountSheet(
    isVisible: Boolean,
    password: String,
    isPasswordVisible: Boolean,
    buttonState: AppButtonState,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmReactivate: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        ReactivateAccountSheetContent(
            password = password,
            isPasswordVisible = isPasswordVisible,
            buttonState = buttonState,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onConfirmReactivate = onConfirmReactivate,
            onForgotPasswordClick = onForgotPasswordClick,
            modifier = modifier
        )
    }
}

@Composable
fun ReactivateAccountSheetContent(
    password: String,
    isPasswordVisible: Boolean,
    buttonState: AppButtonState,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onConfirmReactivate: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.reactivate_account),
            style = Theme.typography.titleLarge,
            color = Theme.colorScheme.primary
        )

        Text(
            text = stringResource(Res.string.deleted_account_found),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onBackground
        )

        Text(
            text = stringResource(Res.string.reactivate_account_prompt),
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurfaceVariant
        )

        CustomTextField(
            value = password,
            onValueChange = onPasswordChange,
            labelText = stringResource(Res.string.password),
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

        Text(
            text = stringResource(Res.string.forget_your_password),
            style = Theme.typography.labelLarge,
            color = Theme.colorScheme.primary,
            modifier = Modifier.clickableNoRipple { onForgotPasswordClick() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButton(
            type = AppButtonType.Primary,
            onClick = onConfirmReactivate,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.reactivate_account),
            state = buttonState
        )
    }
}

@PreviewLightDark
@Composable
private fun ReactivateAccountSheetPreview() = Theme {
    ReactivateAccountSheetContent(
        password = "",
        isPasswordVisible = false,
        buttonState = AppButtonState.Enabled,
        onPasswordChange = {},
        onTogglePasswordVisibility = {},
        onConfirmReactivate = {},
        onForgotPasswordClick = {}
    )
}
