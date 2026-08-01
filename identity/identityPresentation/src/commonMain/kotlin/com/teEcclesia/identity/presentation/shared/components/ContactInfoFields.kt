package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.email_optional
import teecclesia.designsystem.generated.resources.home_phone
import teecclesia.designsystem.generated.resources.phone_number
import teecclesia.designsystem.generated.resources.you_should_have_whatsapp_on_this_phone

@Composable
fun ContactInfoFields(
    phone: String,
    onPhoneChange: (String) -> Unit,
    phoneError: String?,
    homePhone: String,
    onHomePhoneChange: (String) -> Unit,
    homePhoneError: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    modifier: Modifier = Modifier,
    supportingTextPhone: String? = stringResource(Res.string.you_should_have_whatsapp_on_this_phone),
    emailImeAction: ImeAction = ImeAction.Next
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = phone,
            onValueChange = onPhoneChange,
            labelText = stringResource(Res.string.phone_number),
            modifier = Modifier.fillMaxWidth(),
            errorText = phoneError,
            supportingText = supportingTextPhone,
            textStyle = Theme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr),
            prefixText = if (!isRtl) { "+2" } else null,
            suffixText = if (isRtl) { "+2" } else null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )

        CustomTextField(
            value = homePhone,
            onValueChange = onHomePhoneChange,
            labelText = stringResource(Res.string.home_phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            errorText = homePhoneError,
            textStyle = Theme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr),
            prefixText = if (!isRtl) { "02" } else null,
            suffixText = if (isRtl) { "02" } else null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )

        CustomTextField(
            value = email,
            onValueChange = onEmailChange,
            labelText = stringResource(Res.string.email_optional),
            modifier = Modifier.fillMaxWidth(),
            errorText = emailError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = emailImeAction)
        )
    }
}

@Preview
@Composable
private fun ContactInfoFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            ContactInfoFields(
                phone = "01234567890",
                onPhoneChange = {},
                phoneError = null,
                homePhone = "23456789",
                onHomePhoneChange = {},
                homePhoneError = null,
                email = "user@example.com",
                onEmailChange = {},
                emailError = null
            )
        }
    }
}
