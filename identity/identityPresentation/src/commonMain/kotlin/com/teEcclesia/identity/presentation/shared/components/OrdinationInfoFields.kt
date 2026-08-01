package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.radioButton.RadioButton
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.presentation.screen.register.components.FilePickerCard
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.bishop_name
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.no
import teecclesia.designsystem.generated.resources.ordained
import teecclesia.designsystem.generated.resources.ordained_in_this_church
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.ordination_place
import teecclesia.designsystem.generated.resources.ordination_year
import teecclesia.designsystem.generated.resources.rank
import teecclesia.designsystem.generated.resources.upload_ordination_certificate
import teecclesia.designsystem.generated.resources.file_ordination_certificate
import teecclesia.designsystem.generated.resources.yes

@Composable
fun OrdinationInfoFields(
    isOrdained: Boolean,
    onToggleOrdained: (Boolean) -> Unit,
    selectedRank: LookupResponse?,
    ranks: List<LookupResponse>,
    isRankSheetVisible: Boolean,
    onToggleRankSheet: (Boolean) -> Unit,
    onSelectRank: (LookupResponse) -> Unit,
    rankError: String?,
    isOrdainedInThisChurch: Boolean,
    onToggleOrdainedInThisChurch: (Boolean) -> Unit,
    ordinationYear: String,
    onOrdinationYearChange: (String) -> Unit,
    ordinationYearError: String?,
    bishopName: String,
    onBishopNameChange: (String) -> Unit,
    ordinationPlace: String,
    onOrdinationPlaceChange: (String) -> Unit,
    ordinationCertificateFileName: String? = null,
    onUploadOrdinationCertificate: () -> Unit = {},
    onClearOrdinationCertificate: () -> Unit = {},
    onFileClickOrdinationCertificate: (() -> Unit)? = null,
    filePickerContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.ordained),
                style = Theme.typography.bodyLarge,
                color = Theme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = isOrdained,
                        onClick = { onToggleOrdained(true) }
                    )
                    Text(
                        text = stringResource(Res.string.yes),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.clickableNoRipple { onToggleOrdained(true) }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = !isOrdained,
                        onClick = { onToggleOrdained(false) }
                    )
                    Text(
                        text = stringResource(Res.string.no),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onBackground,
                        modifier = Modifier.clickableNoRipple { onToggleOrdained(false) }
                    )
                }
            }
        }

        AnimatedVisibility(visible = isOrdained) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomTextField(
                        value = selectedRank?.name ?: "",
                        onValueChange = {},
                        labelText = stringResource(Res.string.rank),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onToggleRankSheet(true) },
                        readOnly = true,
                        enabled = false,
                        errorText = rankError,
                        trailingIcon = painterResource(Res.drawable.ic_chevron_down)
                    )

                    DropdownMenu(
                        expanded = isRankSheetVisible,
                        onDismissRequest = { onToggleRankSheet(false) },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        ranks.forEach { rank ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        rank.name,
                                        style = Theme.typography.bodyMedium,
                                        color = Theme.colorScheme.onBackground
                                    )
                                },
                                onClick = {
                                    onSelectRank(rank)
                                    onToggleRankSheet(false)
                                }
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.ordained_in_this_church),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onBackground
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = isOrdainedInThisChurch,
                                onClick = { onToggleOrdainedInThisChurch(true) }
                            )
                            Text(
                                stringResource(Res.string.yes),
                                color = Theme.colorScheme.onBackground,
                                style = Theme.typography.bodyMedium
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = !isOrdainedInThisChurch,
                                onClick = { onToggleOrdainedInThisChurch(false) }
                            )
                            Text(
                                stringResource(Res.string.no),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onBackground
                            )
                        }
                    }
                }

                CustomTextField(
                    value = ordinationYear,
                    onValueChange = onOrdinationYearChange,
                    labelText = stringResource(Res.string.ordination_year),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = ordinationYearError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                CustomTextField(
                    value = bishopName,
                    onValueChange = onBishopNameChange,
                    labelText = stringResource(Res.string.bishop_name),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                CustomTextField(
                    value = ordinationPlace,
                    onValueChange = onOrdinationPlaceChange,
                    labelText = stringResource(Res.string.ordination_place),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (filePickerContent != null) {
                    filePickerContent()
                } else {
                    FilePickerCard(
                        modifier = Modifier.padding(top = 8.dp),
                        title = stringResource(Res.string.upload_ordination_certificate),
                        fileTitle = stringResource(Res.string.file_ordination_certificate),
                        fileName = ordinationCertificateFileName,
                        onUploadClick = onUploadOrdinationCertificate,
                        onClearClick = onClearOrdinationCertificate,
                        onFileClick = onFileClickOrdinationCertificate
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OrdinationInfoFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            OrdinationInfoFields(
                isOrdained = true,
                onToggleOrdained = {},
                selectedRank = null,
                ranks = emptyList(),
                isRankSheetVisible = false,
                onToggleRankSheet = {},
                onSelectRank = {},
                rankError = null,
                isOrdainedInThisChurch = true,
                onToggleOrdainedInThisChurch = {},
                ordinationYear = "2020",
                onOrdinationYearChange = {},
                ordinationYearError = null,
                bishopName = "Anba Thomas",
                onBishopNameChange = {},
                ordinationPlace = "Cairo",
                onOrdinationPlaceChange = {},
                ordinationCertificateFileName = null,
                onUploadOrdinationCertificate = {},
                onClearOrdinationCertificate = {}
            )
        }
    }
}
