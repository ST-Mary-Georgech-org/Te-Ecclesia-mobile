package com.teEcclesia.identity.presentation.screen.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.button.AppButton
import com.teEcclesia.designsystem.components.button.AppButtonType
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.menu.DropdownMenu
import com.teEcclesia.designsystem.components.menu.DropdownMenuItem
import com.teEcclesia.designsystem.components.radioButton.RadioButton
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.designsystem.utils.pagination.PaginationTrigger
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.identity.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.RegisterInteractionListener
import com.teEcclesia.identity.presentation.screen.register.RegisterScreenState
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.bishop_name
import teecclesia.designsystem.generated.resources.cancel
import teecclesia.designsystem.generated.resources.deacons_school_details
import teecclesia.designsystem.generated.resources.educational_stage
import teecclesia.designsystem.generated.resources.educational_year
import teecclesia.designsystem.generated.resources.father_deceased
import teecclesia.designsystem.generated.resources.father_phone
import teecclesia.designsystem.generated.resources.father_whatsapp
import teecclesia.designsystem.generated.resources.have_you_attended_a_deacon_school_before
import teecclesia.designsystem.generated.resources.ic_chevron_down
import teecclesia.designsystem.generated.resources.mother_deceased
import teecclesia.designsystem.generated.resources.mother_phone
import teecclesia.designsystem.generated.resources.mother_whatsapp
import teecclesia.designsystem.generated.resources.next
import teecclesia.designsystem.generated.resources.no
import teecclesia.designsystem.generated.resources.ordained
import teecclesia.designsystem.generated.resources.ordained_in_this_church
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.ordination_place
import teecclesia.designsystem.generated.resources.ordination_year
import teecclesia.designsystem.generated.resources.rank
import teecclesia.designsystem.generated.resources.upload_identity_card
import teecclesia.designsystem.generated.resources.upload_ordination_certificate
import teecclesia.designsystem.generated.resources.yes

@Composable
fun RegisterStep4StudentContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.isMale != false) {
                Text(
                    text = stringResource(Res.string.ordination_info),
                    style = Theme.typography.headlineMedium,
                    color = Theme.colorScheme.onBackground
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.ordained),
                        style = Theme.typography.bodyLarge,
                        color = Theme.colorScheme.onBackground
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = state.isOrdained,
                                onClick = { listener.onToggleOrdained(true) }
                            )
                            Text(
                                text = stringResource(Res.string.yes),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onBackground,
                                modifier = Modifier.clickableNoRipple {
                                    listener.onToggleOrdained(
                                        true
                                    )
                                })
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = !state.isOrdained,
                                onClick = { listener.onToggleOrdained(false) }
                            )
                            Text(
                                text = stringResource(Res.string.no),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onBackground,
                                modifier = Modifier.clickableNoRipple {
                                    listener.onToggleOrdained(
                                        false
                                    )
                                })
                        }
                    }
                }

                AnimatedVisibility(visible = state.isOrdained) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CustomTextField(
                                value = state.selectedRank?.name ?: "",
                                onValueChange = {},
                                labelText = stringResource(Res.string.rank),
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    focusManager.clearFocus()
                                    listener.onToggleRankSheet(true)
                                },
                                readOnly = true,
                                enabled = false,
                                errorText = state.rankError?.asString(),
                                trailingIcon = painterResource(Res.drawable.ic_chevron_down)
                            )

                            DropdownMenu(
                                expanded = state.isRankSheetVisible,
                                onDismissRequest = { listener.onToggleRankSheet(false) },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                state.ranks.forEach { rank ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                rank.name,
                                                style = Theme.typography.bodyMedium,
                                                color = Theme.colorScheme.onBackground
                                            )
                                        },
                                        onClick = {
                                            listener.onSelectRank(rank)
                                            listener.onToggleRankSheet(false)
                                        }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(Res.string.ordained_in_this_church),
                                style = Theme.typography.bodyMedium,
                                color = Theme.colorScheme.onBackground
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    RadioButton(
                                        selected = state.isOrdainedInThisChurch,
                                        onClick = { listener.onToggleOrdainedInThisChurch(true) }
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
                                        selected = !state.isOrdainedInThisChurch,
                                        onClick = { listener.onToggleOrdainedInThisChurch(false) }
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
                            value = state.ordinationYear,
                            onValueChange = listener::onOrdinationYearChange,
                            labelText = stringResource(Res.string.ordination_year),
                            modifier = Modifier.fillMaxWidth(),
                            errorText = state.ordinationYearError?.asString(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        CustomTextField(
                            value = state.bishopName,
                            onValueChange = listener::onBishopNameChange,
                            labelText = stringResource(Res.string.bishop_name),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        CustomTextField(
                            value = state.ordinationPlace,
                            onValueChange = listener::onOrdinationPlaceChange,
                            labelText = stringResource(Res.string.ordination_place),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        FilePickerCard(
                            modifier = Modifier.padding(top = 8.dp),
                            title = stringResource(Res.string.upload_ordination_certificate),
                            fileName = state.ordinationCertificateFileName,
                            onUploadClick = { listener.onClickUpload(UploadTarget.ORDINATION_CERTIFICATE) },
                            onClearClick = {
                                listener.onSelectImageBytes(
                                    UploadTarget.ORDINATION_CERTIFICATE,
                                    null,
                                    null
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = stringResource(Res.string.deacons_school_details),
                style = Theme.typography.headlineMedium,
                color = Theme.colorScheme.onBackground
            )

            Column {
                Text(
                    text = stringResource(Res.string.have_you_attended_a_deacon_school_before),
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
                            selected = state.shamamsaStatus == ShamamsaStudyStatus.YES,
                            onClick = { listener.onShamamsaStatusSelected(ShamamsaStudyStatus.YES) }
                        )
                        Text(
                            stringResource(Res.string.yes),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onBackground
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = state.shamamsaStatus == ShamamsaStudyStatus.NO,
                            onClick = { listener.onShamamsaStatusSelected(ShamamsaStudyStatus.NO) }
                        )
                        Text(
                            stringResource(Res.string.no),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onBackground
                        )
                    }
                }
            }


            Box(modifier = Modifier.fillMaxWidth()) {
                CustomTextField(
                    value = state.studentEducationalStage?.name ?: "",
                    onValueChange = {},
                    labelText = stringResource(Res.string.educational_stage),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        focusManager.clearFocus()
                        listener.onToggleStageSheet(true)
                    },
                    readOnly = true,
                    enabled = false,
                    errorText = state.stageError?.asString(),
                    trailingIcon = painterResource(Res.drawable.ic_chevron_down)
                )

                BottomSheet(
                    isVisible = state.isStageSheetVisible,
                    onDismiss = { listener.onToggleStageSheet(false) }
                ) {
                    val stageListState = rememberLazyListState()

                    Text(
                        text = stringResource(Res.string.educational_stage),
                        style = Theme.typography.headlineSmall,
                        color = Theme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        state = stageListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = state.educationalStages,
                            key = { stage -> stage.id }
                        ) { stage ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableNoRipple {
                                        listener.onSelectEducationalStage(stage)
                                        listener.onToggleStageSheet(false)
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stage.name,
                                    style = Theme.typography.bodyLarge,
                                    color = Theme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    PaginationTrigger(
                        list = state.educationalStages,
                        listState = stageListState,
                        remainingItemsToLoadNextPage = 5,
                        loadNextItems = listener::onLoadNextEducationalStages
                    )
                }
            }

            val hasYears =
                !state.studentEducationalStage?.subItems.isNullOrEmpty() || state.studentEducationalYear != null

            AnimatedVisibility(
                visible = hasYears,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomTextField(
                        value = state.studentEducationalYear?.name ?: "",
                        onValueChange = {},
                        labelText = stringResource(Res.string.educational_year),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            focusManager.clearFocus()
                            listener.onToggleYearSheet(true)
                        },
                        readOnly = true,
                        enabled = false,
                        errorText = state.yearError?.asString(),
                        trailingIcon = painterResource(Res.drawable.ic_chevron_down),
                    )

                    BottomSheet(
                        isVisible = state.isYearSheetVisible,
                        onDismiss = { listener.onToggleYearSheet(false) }
                    ) {
                        Text(
                            text = stringResource(Res.string.educational_year),
                            style = Theme.typography.headlineSmall,
                            color = Theme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = false),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(
                                items = state.studentEducationalStage?.subItems ?: emptyList(),
                                key = { year -> year.id }
                            ) { year ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickableNoRipple {
                                            listener.onSelectEducationalYear(year)
                                            listener.onToggleYearSheet(false)
                                        }
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = year.name,
                                        style = Theme.typography.bodyLarge,
                                        color = Theme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.isFatherDeceased,
                    onCheckedChange = listener::onToggleFatherDeceased
                )
                Text(
                    text = stringResource(Res.string.father_deceased),
                    style = Theme.typography.bodyMedium, color = Theme.colorScheme.onBackground,
                    modifier = Modifier.clickableNoRipple { listener.onToggleFatherDeceased(!state.isFatherDeceased) })
            }

            AnimatedVisibility(visible = !state.isFatherDeceased) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomTextField(
                        value = state.fatherPhone,
                        onValueChange = listener::onFatherPhoneChange,
                        labelText = stringResource(Res.string.father_phone),
                        modifier = Modifier.fillMaxWidth(),
                        errorText = state.fatherPhoneError?.asString(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    CustomTextField(
                        value = state.fatherWhatsapp,
                        onValueChange = listener::onFatherWhatsappChange,
                        labelText = stringResource(Res.string.father_whatsapp),
                        modifier = Modifier.fillMaxWidth(),
                        errorText = state.fatherWhatsappError?.asString(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.isMotherDeceased,
                    onCheckedChange = listener::onToggleMotherDeceased
                )
                Text(
                    text = stringResource(Res.string.mother_deceased),
                    style = Theme.typography.bodyMedium, color = Theme.colorScheme.onBackground,
                    modifier = Modifier.clickableNoRipple { listener.onToggleMotherDeceased(!state.isMotherDeceased) })
            }

            AnimatedVisibility(visible = !state.isMotherDeceased) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomTextField(
                        value = state.motherPhone,
                        onValueChange = listener::onMotherPhoneChange,
                        labelText = stringResource(Res.string.mother_phone),
                        modifier = Modifier.fillMaxWidth(),
                        errorText = state.motherPhoneError.asString(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    CustomTextField(
                        value = state.motherWhatsapp,
                        onValueChange = listener::onMotherWhatsappChange,
                        labelText = stringResource(Res.string.mother_whatsapp),
                        modifier = Modifier.fillMaxWidth(),
                        errorText = state.motherWhatsappError?.asString(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            FilePickerCard(
                modifier = Modifier.padding(top = 8.dp),
                title = stringResource(Res.string.upload_identity_card),
                fileName = state.identityCertificateFileName,
                onUploadClick = { listener.onClickUpload(UploadTarget.IDENTITY_CERTIFICATE) },
                onClearClick = {
                    listener.onSelectImageBytes(
                        UploadTarget.IDENTITY_CERTIFICATE,
                        null,
                        null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                type = AppButtonType.Secondary,
                onClick = listener::onClickPreviousStep,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.cancel)
            )

            AppButton(
                type = AppButtonType.Primary,
                onClick = listener::onClickNextStep,
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.next),
                state = state.actionButtonState
            )
        }
    }
}

@Preview(heightDp = 1600)
@Composable
private fun RegisterStep4StudentContentPreviewLightDark() {
    var state by remember {
        mutableStateOf(
            RegisterScreenState(
                currentStep = 4,
                selectedRole = UserRole.MAKHDOOM,
                isMale = true
            )
        )
    }
    val listener = remember(state) {
        object : RegisterInteractionListener {
            override fun onClickNextStep() {}
            override fun onClickPreviousStep() {}
            override fun onClickLogin() {}
            override fun onFirstNameChange(value: String) {}
            override fun onSecondNameChange(value: String) {}
            override fun onThirdNameChange(value: String) {}
            override fun onLastNameChange(value: String) {}
            override fun onDisplayNameChange(value: String) {}
            override fun onNationalIdChange(value: String) {}
            override fun onJobChange(value: String) {}
            override fun onSelectConfessionPriest(priest: Priest?) {}
            override fun onSelectFromAnotherChurch() {}
            override fun onExternalPriestNameChange(value: String) {}
            override fun onExternalPriestChurchChange(value: String) {}
            override fun onExternalPriestPhoneChange(value: String) {}
            override fun onTogglePriestSheet(visible: Boolean) {}
            override fun onPhoneChange(value: String) {}
            override fun onHomePhoneChange(value: String) {}
            override fun onEmailChange(value: String) {}
            override fun onPasswordChange(value: String) {}
            override fun onTogglePasswordVisibility() {}
            override fun onBuildingNoChange(value: String) {}
            override fun onStreetChange(value: String) {}
            override fun onStreetBranchChange(value: String) {}
            override fun onAreaChange(value: String) {}
            override fun onSelectArea(area: String) {}
            override fun onToggleAreaSheet(visible: Boolean) {}
            override fun onFloorChange(value: String) {}
            override fun onApartmentChange(value: String) {}
            override fun onSpecialMarkChange(value: String) {}
            override fun onRoleSelected(role: UserRole) {}
            override fun onToggleOrdained(ordained: Boolean) {
                state = state.copy(
                    isOrdained = ordained,
                    ordinationYearError = if (!ordained) null else state.ordinationYearError,
                    rankError = if (!ordained) null else state.rankError
                )
            }

            override fun onSelectRank(rank: LookupResponse) {
                state = state.copy(selectedRank = rank)
            }

            override fun onToggleRankSheet(visible: Boolean) {
                state = state.copy(isRankSheetVisible = visible)
            }

            override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {
                state = state.copy(isOrdainedInThisChurch = inThisChurch)
            }

            override fun onOrdinationYearChange(value: String) {
                state = state.copy(ordinationYear = value, ordinationYearError = null)
            }

            override fun onBishopNameChange(value: String) {
                state = state.copy(bishopName = value)
            }

            override fun onOrdinationPlaceChange(value: String) {
                state = state.copy(ordinationPlace = value)
            }

            override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {
                state = state.copy(shamamsaStatus = status)
            }

            override fun onSelectEducationalStage(stage: LookupResponse) {}
            override fun onToggleStageSheet(visible: Boolean) {
                state = state.copy(isStageSheetVisible = visible)
            }

            override fun onSelectEducationalYear(year: LookupResponse) {}
            override fun onToggleYearSheet(visible: Boolean) {
                state = state.copy(isYearSheetVisible = visible)
            }

            override fun onToggleFatherDeceased(deceased: Boolean) {
                state = state.copy(
                    isFatherDeceased = deceased,
                    fatherPhoneError = if (deceased) null else state.fatherPhoneError,
                    fatherWhatsappError = if (deceased) null else state.fatherWhatsappError
                )
            }

            override fun onFatherPhoneChange(value: String) {
                state = state.copy(fatherPhone = value, fatherPhoneError = null)
            }

            override fun onFatherWhatsappChange(value: String) {
                state = state.copy(fatherWhatsapp = value, fatherWhatsappError = null)
            }

            override fun onToggleMotherDeceased(deceased: Boolean) {
                state = state.copy(
                    isMotherDeceased = deceased,
                    motherPhoneError = if (deceased) null else state.motherPhoneError,
                    motherWhatsappError = if (deceased) null else state.motherWhatsappError
                )
            }

            override fun onMotherPhoneChange(value: String) {
                state = state.copy(motherPhone = value, motherPhoneError = null)
            }

            override fun onMotherWhatsappChange(value: String) {
                state = state.copy(motherWhatsapp = value, motherWhatsappError = null)
            }

            override fun onPartnerQueryChange(query: String) {}
            override fun onSearchPartner() {}
            override fun onRemovePartner() {}
            override fun onChildQueryChange(query: String) {}
            override fun onSearchChild() {}
            override fun onRemoveChild(child: UserSummary) {}
            override fun onClickUpload(target: UploadTarget) {}
            override fun onDismissUploadBottomSheet() {}
            override fun onSelectImageBytes(
                target: UploadTarget,
                bytes: ByteArray?,
                fileName: String?
            ) {
            }

            override fun onClickVerifyWhatsApp() {}
            override fun onClickCheckWhatsAppStatus() {}
            override fun onLoadNextPriests() {}
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterStep4StudentContent(state = state, listener = listener)
        }
    }
}
