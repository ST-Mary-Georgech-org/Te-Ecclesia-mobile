package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.components.UserChip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.search_partner
import teecclesia.designsystem.generated.resources.search_partner_support_text

@Composable
fun PartnerSelectionFields(
    selectedPartner: UserSummary?,
    partnerQuery: String,
    onPartnerQueryChange: (String) -> Unit,
    onSearchPartner: () -> Unit,
    onRemovePartner: () -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    if (selectedPartner == null) {
        CustomTextField(
            value = partnerQuery,
            onValueChange = onPartnerQueryChange,
            labelText = stringResource(Res.string.search_partner),
            supportingText = stringResource(Res.string.search_partner_support_text),
            errorText = errorText,
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = painterResource(Res.drawable.ic_plus),
            onTrailingIconClick = onSearchPartner,
        )
    } else {
        UserChip(
            user = selectedPartner,
            onRemove = onRemovePartner,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PartnerSelectionFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            PartnerSelectionFields(
                selectedPartner = null,
                partnerQuery = "",
                onPartnerQueryChange = {},
                onSearchPartner = {},
                onRemovePartner = {}
            )
        }
    }
}
