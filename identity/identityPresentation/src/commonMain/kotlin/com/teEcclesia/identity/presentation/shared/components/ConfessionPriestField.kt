package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.Priest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.confession_priest
import teecclesia.designsystem.generated.resources.confession_priest_church
import teecclesia.designsystem.generated.resources.confession_priest_name
import teecclesia.designsystem.generated.resources.confession_priest_phone
import teecclesia.designsystem.generated.resources.from_another_church
import teecclesia.designsystem.generated.resources.ic_chevron_down

@Composable
fun ConfessionPriestField(
    selectedPriestName: String,
    isFromAnotherChurch: Boolean,
    isPriestSheetVisible: Boolean,
    confessionPriests: List<Priest>,
    confessionPriestError: String?,
    externalPriestName: String,
    externalPriestNameError: String?,
    externalPriestChurch: String,
    externalPriestChurchError: String?,
    externalPriestPhone: String,
    externalPriestPhoneError: String?,
    onTogglePriestSheet: (Boolean) -> Unit,
    onSelectConfessionPriest: (Priest) -> Unit,
    onSelectFromAnotherChurch: () -> Unit,
    onLoadNextPriests: () -> Unit,
    onExternalPriestNameChange: (String) -> Unit,
    onExternalPriestChurchChange: (String) -> Unit,
    onExternalPriestPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val priestText = when {
                isFromAnotherChurch -> stringResource(Res.string.from_another_church)
                else -> selectedPriestName
            }

            val interactionSource = remember { MutableInteractionSource() }

            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                    if (interaction is PressInteraction.Release) {
                        onTogglePriestSheet(true)
                    }
                }
            }

            CustomTextField(
                value = priestText,
                onValueChange = {},
                labelText = stringResource(Res.string.confession_priest),
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                errorText = confessionPriestError,
                trailingIcon = painterResource(Res.drawable.ic_chevron_down),
                onClick = {
                    onTogglePriestSheet(true)
                }
            )

            BottomSheet(
                isVisible = isPriestSheetVisible,
                onDismiss = { onTogglePriestSheet(false) }
            ) {
                val priestListState = rememberLazyListState()

                Text(
                    text = stringResource(Res.string.confession_priest),
                    style = Theme.typography.headlineSmall,
                    color = Theme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    state = priestListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = confessionPriests,
                        key = { priest -> priest.id }
                    ) { priest ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableNoRipple {
                                    onSelectConfessionPriest(priest)
                                    onTogglePriestSheet(false)
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = priest.name,
                                style = Theme.typography.bodyLarge,
                                color = Theme.colorScheme.onSurface
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableNoRipple {
                                    onSelectFromAnotherChurch()
                                    onTogglePriestSheet(false)
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.from_another_church),
                                style = Theme.typography.bodyLarge,
                                color = Theme.colorScheme.primary
                            )
                        }
                    }
                }

                PaginationTrigger(
                    list = confessionPriests,
                    listState = priestListState,
                    remainingItemsToLoadNextPage = 5,
                    loadNextItems = onLoadNextPriests
                )
            }
        }

        AnimatedVisibility(
            visible = isFromAnotherChurch,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomTextField(
                    value = externalPriestName,
                    onValueChange = onExternalPriestNameChange,
                    labelText = stringResource(Res.string.confession_priest_name),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = externalPriestNameError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                CustomTextField(
                    value = externalPriestChurch,
                    onValueChange = onExternalPriestChurchChange,
                    labelText = stringResource(Res.string.confession_priest_church),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = externalPriestChurchError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

                CustomTextField(
                    value = externalPriestPhone,
                    onValueChange = onExternalPriestPhoneChange,
                    labelText = stringResource(Res.string.confession_priest_phone),
                    modifier = Modifier.fillMaxWidth(),
                    errorText = externalPriestPhoneError,
                    singleLine = true,
                    textStyle = Theme.typography.bodyLarge.copy(
                        textDirection = TextDirection.Ltr
                    ),
                    prefixText = if (!isRtl) { "+2" } else null,
                    suffixText = if (isRtl) { "+2" } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConfessionPriestFieldPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            ConfessionPriestField(
                selectedPriestName = "Father Mark",
                isFromAnotherChurch = false,
                isPriestSheetVisible = false,
                confessionPriests = emptyList(),
                confessionPriestError = null,
                externalPriestName = "",
                externalPriestNameError = null,
                externalPriestChurch = "",
                externalPriestChurchError = null,
                externalPriestPhone = "",
                externalPriestPhoneError = null,
                onTogglePriestSheet = {},
                onSelectConfessionPriest = {},
                onSelectFromAnotherChurch = {},
                onLoadNextPriests = {},
                onExternalPriestNameChange = {},
                onExternalPriestChurchChange = {},
                onExternalPriestPhoneChange = {}
            )
        }
    }
}
