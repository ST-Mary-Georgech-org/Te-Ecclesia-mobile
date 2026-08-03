package com.teEcclesia.identity.presentation.screen.register

import com.teEcclesia.designsystem.components.navigation.BackHandler

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.utils.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.CircularProgressIndicator
import com.teEcclesia.designsystem.components.button.AppButtonState
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.indicator.LinearProgressIndicator
import com.teEcclesia.designsystem.components.indicator.PullToRefresh
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.screen.register.components.FilePickerBottomSheet
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep1Content
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep2Content
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep3Content
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep4KahenContent
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep4ParentContent
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep4ServantContent
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep4StudentContent
import com.teEcclesia.identity.presentation.screen.register.components.RegisterStep5VerifyContent
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.registration

@Composable
fun RegisterScreen(
    isEditMode: Boolean = false,
    viewModel: RegisterViewModel = koinViewModel(parameters = { parametersOf(isEditMode) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegisterScreenContent(
        state = state,
        listener = viewModel,
        onFileOptionPicked = viewModel::onFileOptionPicked
    )
}

@Composable
fun RegisterScreenContent(
    state: RegisterScreenState,
    listener: RegisterInteractionListener,
    onFileOptionPicked: (FilePickOption) -> Unit = {}
) {
    BackHandler(enabled = state.currentStep > 1) {
        listener.onClickPreviousStep()
    }

    FilePickerBottomSheet(
        isVisible = state.isUploadBottomSheetVisible,
        target = state.activeUploadTarget,
        onDismiss = listener::onDismissUploadBottomSheet,
        onOptionSelected = onFileOptionPicked
    )

    val fileOpener = com.teEcclesia.identity.presentation.util.rememberFileOpener()

    PullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = listener::onRefresh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = Theme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickableNoRipple(onClick = listener::onClickPreviousStep)
                        .padding(end = 16.dp)
                )
                Text(
                    text = stringResource(Res.string.registration),
                    style = Theme.typography.headlineMedium,
                    color = Theme.colorScheme.onBackground
                )
            }

            val animatedProgress by animateFloatAsState(
                targetValue = state.currentStep / 5f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                label = "ProgressAnimation"
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(4.dp)
            )

            if (state.isLoading && state.actionButtonState != AppButtonState.Loading && !state.isRefreshing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    AnimatedContent(
                        targetState = state.currentStep,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "RegisterStepTransition"
                    ) { targetStep ->
                        when (targetStep) {
                            1 -> RegisterStep1Content(state = state, listener = listener)
                            2 -> RegisterStep2Content(state = state, listener = listener)
                            3 -> RegisterStep3Content(state = state, listener = listener)
                            4 -> when (state.selectedRole) {
                                UserRole.MAKHDOOM -> RegisterStep4StudentContent(
                                    state = state,
                                    listener = listener,
                                    onFileClickOrdinationCertificate = {
                                        fileOpener.openFile(
                                            bytes = state.ordinationCertificateBytes,
                                            fileName = state.ordinationCertificateFileName
                                        )
                                    },
                                    onFileClickIdentityCertificate = {
                                        fileOpener.openFile(
                                            bytes = state.identityCertificateBytes,
                                            fileName = state.identityCertificateFileName
                                        )
                                    }
                                )
                                UserRole.KHADEM -> RegisterStep4ServantContent(state = state, listener = listener)
                                UserRole.KAHEN -> RegisterStep4KahenContent(state = state, listener = listener)
                                UserRole.PARENT -> RegisterStep4ParentContent(
                                    state = state,
                                    listener = listener,
                                    onFileClickIdentityCertificate = {
                                        fileOpener.openFile(
                                            bytes = state.identityCertificateBytes,
                                            fileName = state.identityCertificateFileName
                                        )
                                    }
                                )
                                else -> RegisterStep4ServantContent(state = state, listener = listener)
                            }
                            5 -> RegisterStep5VerifyContent(state = state, listener = listener)
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    var state by remember { mutableStateOf(RegisterScreenState()) }
    val listener = remember(state) {
        object : RegisterInteractionListener {
            override fun onClickNextStep() {
                if (state.currentStep < 5) state = state.copy(currentStep = state.currentStep + 1)
            }
            override fun onClickPreviousStep() {
                if (state.currentStep > 1) state = state.copy(currentStep = state.currentStep - 1)
            }
            override fun onClickLogin() {}
            override fun onFirstNameChange(value: String) { state = state.copy(firstName = value) }
            override fun onSecondNameChange(value: String) { state = state.copy(secondName = value) }
            override fun onThirdNameChange(value: String) { state = state.copy(thirdName = value) }
            override fun onLastNameChange(value: String) { state = state.copy(lastName = value) }
            override fun onDisplayNameChange(value: String) { state = state.copy(displayName = value) }
            override fun onNationalIdChange(value: String) { state = state.copy(nationalId = value) }
            override fun onJobChange(value: String) { state = state.copy(job = value) }
            override fun onSelectConfessionPriest(priest: Priest?) { state = state.copy(selectedConfessionPriest = priest) }
            override fun onSelectFromAnotherChurch() { state = state.copy(isFromAnotherChurch = true) }
            override fun onExternalPriestNameChange(value: String) { state = state.copy(externalPriestName = value) }
            override fun onExternalPriestChurchChange(value: String) { state = state.copy(externalPriestChurch = value) }
            override fun onExternalPriestPhoneChange(value: String) { state = state.copy(externalPriestPhone = value) }
            override fun onTogglePriestSheet(visible: Boolean) { state = state.copy(isPriestSheetVisible = visible) }
            override fun onPhoneChange(value: String) { state = state.copy(phone = value) }
            override fun onHomePhoneChange(value: String) { state = state.copy(homePhone = value) }
            override fun onEmailChange(value: String) { state = state.copy(email = value) }
            override fun onPasswordChange(value: String) { state = state.copy(password = value) }
            override fun onTogglePasswordVisibility() { state = state.copy(isPasswordVisible = !state.isPasswordVisible) }
            override fun onBuildingNoChange(value: String) { state = state.copy(buildingNo = value) }
            override fun onStreetChange(value: String) { state = state.copy(street = value) }
            override fun onStreetBranchChange(value: String) { state = state.copy(streetBranch = value) }
            override fun onAreaChange(value: String) { state = state.copy(selectedArea = value) }
            override fun onSelectArea(area: String) { state = state.copy(selectedArea = area) }
            override fun onToggleAreaSheet(visible: Boolean) { state = state.copy(isAreaSheetVisible = visible) }
            override fun onFloorChange(value: String) { state = state.copy(floor = value) }
            override fun onApartmentChange(value: String) { state = state.copy(apartment = value) }
            override fun onSpecialMarkChange(value: String) { state = state.copy(specialMark = value) }
            override fun onRoleSelected(role: UserRole) { state = state.copy(selectedRole = role) }
            override fun onToggleOrdained(ordained: Boolean) { state = state.copy(isOrdained = ordained, ordinationYearError = if (!ordained) null else state.ordinationYearError, rankError = if (!ordained) null else state.rankError) }
            override fun onSelectRank(rank: LookupResponse) { state = state.copy(selectedRank = rank) }
            override fun onToggleRankSheet(visible: Boolean) { state = state.copy(isRankSheetVisible = visible) }
            override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) { state = state.copy(isOrdainedInThisChurch = inThisChurch) }
            override fun onOrdinationYearChange(value: String) { state = state.copy(ordinationYear = value, ordinationYearError = null) }
            override fun onBishopNameChange(value: String) { state = state.copy(bishopName = value) }
            override fun onOrdinationPlaceChange(value: String) { state = state.copy(ordinationPlace = value) }
            override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) { state = state.copy(shamamsaStatus = status) }
            override fun onSelectEducationalStage(stage: LookupResponse) {  }
            override fun onToggleStageSheet(visible: Boolean) { state = state.copy(isStageSheetVisible = visible) }
            override fun onSelectEducationalYear(year: LookupResponse) {  }
            override fun onToggleYearSheet(visible: Boolean) { state = state.copy(isYearSheetVisible = visible) }
            override fun onToggleFatherDeceased(deceased: Boolean) { state = state.copy(isFatherDeceased = deceased, fatherPhoneError = if (deceased) null else state.fatherPhoneError, fatherWhatsappError = if (deceased) null else state.fatherWhatsappError) }
            override fun onFatherPhoneChange(value: String) { state = state.copy(fatherPhone = value, fatherPhoneError = null) }
            override fun onFatherWhatsappChange(value: String) { state = state.copy(fatherWhatsapp = value, fatherWhatsappError = null) }
            override fun onToggleMotherDeceased(deceased: Boolean) { state = state.copy(isMotherDeceased = deceased, motherPhoneError = if (deceased) null else state.motherPhoneError, motherWhatsappError = if (deceased) null else state.motherWhatsappError) }
            override fun onMotherPhoneChange(value: String) { state = state.copy(motherPhone = value, motherPhoneError = null) }
            override fun onMotherWhatsappChange(value: String) { state = state.copy(motherWhatsapp = value, motherWhatsappError = null) }
            override fun onPartnerQueryChange(query: String) { state = state.copy(partnerQuery = query) }
            override fun onSearchPartner() {}
            override fun onRemovePartner() { state = state.copy(selectedPartner = null) }
            override fun onChildQueryChange(query: String) { state = state.copy(childQuery = query) }
            override fun onSearchChild() {}
            override fun onRemoveChild(child: UserSummary) { state = state.copy(selectedChildren = state.selectedChildren - child) }
            override fun onClickUpload(target: UploadTarget) {}
            override fun onDismissUploadBottomSheet() {}
            override fun onSelectImageBytes(target: UploadTarget, bytes: ByteArray?, fileName: String?) {}
            override fun onClickVerifyWhatsApp() {}
            override fun onClickCheckWhatsAppStatus() {}
            override fun onLoadNextPriests() {}
            override fun onLoadNextRanks() {}
            override fun onLoadNextEducationalStages() {}
            override fun onRefresh() {}
        }
    }
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            RegisterScreenContent(
                state = state,
                listener = listener
            )
        }
    }
}
