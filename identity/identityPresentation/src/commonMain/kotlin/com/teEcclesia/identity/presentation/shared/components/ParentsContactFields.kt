package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.presentation.screen.register.components.FilePickerCard
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.father_deceased
import teecclesia.designsystem.generated.resources.father_phone
import teecclesia.designsystem.generated.resources.father_whatsapp
import teecclesia.designsystem.generated.resources.mother_deceased
import teecclesia.designsystem.generated.resources.mother_phone
import teecclesia.designsystem.generated.resources.mother_whatsapp
import teecclesia.designsystem.generated.resources.upload_identity_card
import teecclesia.designsystem.generated.resources.file_identity_card

@Composable
fun ParentsContactFields(
    isFatherDeceased: Boolean,
    onToggleFatherDeceased: (Boolean) -> Unit,
    fatherPhone: String,
    onFatherPhoneChange: (String) -> Unit,
    fatherPhoneError: String?,
    fatherWhatsapp: String,
    onFatherWhatsappChange: (String) -> Unit,
    fatherWhatsappError: String?,
    isMotherDeceased: Boolean,
    onToggleMotherDeceased: (Boolean) -> Unit,
    motherPhone: String,
    onMotherPhoneChange: (String) -> Unit,
    motherPhoneError: String?,
    motherWhatsapp: String,
    onMotherWhatsappChange: (String) -> Unit,
    motherWhatsappError: String?,
    identityCertificateFileName: String? = null,
    identityCertificateError: String? = null,
    onUploadIdentityCertificate: () -> Unit = {},
    onClearIdentityCertificate: () -> Unit = {},
    onFileClickIdentityCertificate: (() -> Unit)? = null,
    filePickerContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isFatherDeceased,
                onCheckedChange = onToggleFatherDeceased
            )
            Text(
                text = stringResource(Res.string.father_deceased),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.clickableNoRipple { onToggleFatherDeceased(!isFatherDeceased) }
            )
        }

        AnimatedVisibility(visible = !isFatherDeceased) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = fatherPhone,
                    onValueChange = onFatherPhoneChange,
                    labelText = stringResource(Res.string.father_phone),
                    modifier = Modifier.fillMaxWidth(),
                    prefixText = if (!isRtl) { "+2" } else null,
                    suffixText = if (isRtl) { "+2" } else null,
                    errorText = fatherPhoneError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                CustomTextField(
                    value = fatherWhatsapp,
                    onValueChange = onFatherWhatsappChange,
                    labelText = stringResource(Res.string.father_whatsapp),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = fatherWhatsappError,
                    prefixText = if (!isRtl) { "+2" } else null,
                    suffixText = if (isRtl) { "+2" } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isMotherDeceased,
                onCheckedChange = onToggleMotherDeceased
            )
            Text(
                text = stringResource(Res.string.mother_deceased),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onBackground,
                modifier = Modifier.clickableNoRipple { onToggleMotherDeceased(!isMotherDeceased) }
            )
        }

        AnimatedVisibility(visible = !isMotherDeceased) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = motherPhone,
                    onValueChange = onMotherPhoneChange,
                    labelText = stringResource(Res.string.mother_phone),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = motherPhoneError,
                    prefixText = if (!isRtl) { "+2" } else null,
                    suffixText = if (isRtl) { "+2" } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                CustomTextField(
                    value = motherWhatsapp,
                    onValueChange = onMotherWhatsappChange,
                    labelText = stringResource(Res.string.mother_whatsapp),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = motherWhatsappError,
                    prefixText = if (!isRtl) { "+2" } else null,
                    suffixText = if (isRtl) { "+2" } else null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
        }

        if (filePickerContent != null) {
            filePickerContent()
        } else {
            FilePickerCard(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(Res.string.upload_identity_card),
                fileTitle = stringResource(Res.string.file_identity_card),
                fileName = identityCertificateFileName,
                onUploadClick = onUploadIdentityCertificate,
                onClearClick = onClearIdentityCertificate,
                onFileClick = onFileClickIdentityCertificate,
                errorText = identityCertificateError
            )
        }
    }
}

@Preview
@Composable
private fun ParentsContactFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            ParentsContactFields(
                isFatherDeceased = false,
                onToggleFatherDeceased = {},
                fatherPhone = "",
                onFatherPhoneChange = {},
                fatherPhoneError = null,
                fatherWhatsapp = "",
                onFatherWhatsappChange = {},
                fatherWhatsappError = null,
                isMotherDeceased = false,
                onToggleMotherDeceased = {},
                motherPhone = "",
                onMotherPhoneChange = {},
                motherPhoneError = null,
                motherWhatsapp = "",
                onMotherWhatsappChange = {},
                motherWhatsappError = null,
                identityCertificateFileName = null,
                onUploadIdentityCertificate = {},
                onClearIdentityCertificate = {}
            )
        }
    }
}
