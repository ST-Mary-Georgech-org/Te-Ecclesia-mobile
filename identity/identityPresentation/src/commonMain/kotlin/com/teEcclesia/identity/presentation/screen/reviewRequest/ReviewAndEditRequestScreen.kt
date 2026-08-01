package com.teEcclesia.identity.presentation.screen.reviewRequest

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teEcclesia.designsystem.components.icon.Icon
import com.teEcclesia.designsystem.components.indicator.LinearProgressIndicator
import com.teEcclesia.designsystem.components.navigation.BackHandler
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.domain.model.Priest
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import com.teEcclesia.shared.domain.model.UserRole
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.register.components.FilePickOption
import com.teEcclesia.identity.presentation.screen.register.components.FilePickerBottomSheet
import com.teEcclesia.identity.presentation.screen.reviewRequest.components.ReviewStep1Content
import com.teEcclesia.identity.presentation.screen.reviewRequest.components.ReviewStep2Content
import com.teEcclesia.lookups.domain.model.LookupResponse
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_arrow_back
import teecclesia.designsystem.generated.resources.review_and_edit_request
import teecclesia.designsystem.generated.resources.add_new_user
import teecclesia.designsystem.generated.resources.step_1_of_2
import teecclesia.designsystem.generated.resources.step_2_of_2

@Composable
fun ReviewAndEditRequestScreen(
    userId: String? = null,
    viewModel: ReviewAndEditRequestViewModel = koinViewModel(parameters = { parametersOf(userId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReviewAndEditRequestContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun ReviewAndEditRequestContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener
) {
    BackHandler(enabled = state.canGoPrevious) {
        listener.onPreviousStep()
    }

    FilePickerBottomSheet(
        isVisible = state.isUploadBottomSheetVisible,
        target = state.activeUploadTarget,
        onDismiss = listener::onDismissUploadBottomSheet,
        onOptionSelected = listener::onFileOptionPicked
    )



    val fileOpener = com.teEcclesia.identity.presentation.util.rememberFileOpener()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Theme.colorScheme.primary)
            }
        } else {
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
                        .clickableNoRipple(onClick = listener::onPreviousStep)
                        .padding(end = 16.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (state.userId.isNotBlank()) stringResource(Res.string.review_and_edit_request) else stringResource(Res.string.add_new_user),
                        style = Theme.typography.headlineMedium,
                        color = Theme.colorScheme.onBackground
                    )
                    Text(
                        text = if (state.currentStep == 1) stringResource(Res.string.step_1_of_2) else stringResource(Res.string.step_2_of_2),
                        style = Theme.typography.bodySmall,
                        color = Theme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LinearProgressIndicator(
                progress = { if (state.currentStep == 1) 0.5f else 1f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 16.dp),
                color = Theme.colorScheme.primary,
                trackColor = Theme.colorScheme.surfaceContainerHighest
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "StepTransition"
                ) { step ->
                    when (step) {
                        1 -> ReviewStep1Content(state = state, listener = listener)
                        2 -> ReviewStep2Content(
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
                    }
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun ReviewAndEditRequestScreenPreview() {
    val state = ReviewAndEditRequestUiState(
        userId = "123",
        isLoading = false
    )
    val listener = object : ReviewAndEditRequestInteractionListener {
        override fun onClickBack() {}
        override fun onNextStep() {}
        override fun onPreviousStep() {}
        override fun onStepClicked(step: Int) {}
        override fun onApproveRequest() {}
        override fun onRejectRequest(reason: String) {}
        override fun onToggleRejectDialog(isVisible: Boolean) {}
        override fun onRefresh() {}
        override fun onClickUpload(target: UploadTarget) {}
        override fun onDismissUploadBottomSheet() {}

        override fun onCodeChanged(value: String) {}
        override fun onFirstNameChanged(value: String) {}
        override fun onSecondNameChanged(value: String) {}
        override fun onThirdNameChanged(value: String) {}
        override fun onLastNameChanged(value: String) {}
        override fun onDisplayNameChanged(value: String) {}
        override fun onNationalIdChanged(value: String) {}
        override fun onJobChanged(value: String) {}
        override fun onIsFromAnotherChurchChanged(value: Boolean) {}
        override fun onConfessionPriestIdChanged(value: String?) {}
        override fun onConfessionPriestNameChanged(value: String) {}
        override fun onConfessionPriestChurchChanged(value: String) {}
        override fun onConfessionPriestPhoneChanged(value: String) {}
        override fun onPhoneChanged(value: String) {}
        override fun onHomePhoneChanged(value: String) {}
        override fun onEmailChanged(value: String) {}
        override fun onBuildingNoChanged(value: String) {}
        override fun onStreetChanged(value: String) {}
        override fun onStreetBranchChanged(value: String) {}
        override fun onAreaChanged(value: String) {}
        override fun onFloorChanged(value: String) {}
        override fun onApartmentChanged(value: String) {}
        override fun onSpecialMarkChanged(value: String) {}
        override fun onSelectArea(area: String) {}
        override fun onToggleAreaSheet(visible: Boolean) {}
        override fun onSelectConfessionPriest(priest: Priest?) {}
        override fun onSelectFromAnotherChurch() {}
        override fun onTogglePriestSheet(visible: Boolean) {}
        override fun onLoadNextPriests() {}
        override fun onFileOptionPicked(option: FilePickOption) {}

        override fun onSelectImageBytes(
            target: UploadTarget,
            bytes: ByteArray?,
            fileName: String?
        ) {
        }

        override fun onRoleSelected(role: UserRole) {}
        override fun onToggleRoleSheet(visible: Boolean) {}
        override fun onShamamsaStatusSelected(status: ShamamsaStudyStatus) {}
        override fun onToggleOrdained(ordained: Boolean) {}
        override fun onSelectRank(rank: LookupResponse) {}
        override fun onToggleRankSheet(visible: Boolean) {}
        override fun onToggleOrdainedInThisChurch(inThisChurch: Boolean) {}
        override fun onOrdinationYearChange(value: String) {}
        override fun onBishopNameChange(value: String) {}
        override fun onOrdinationPlaceChange(value: String) {}
        override fun onSelectEducationalStage(stage: LookupResponse) {}
        override fun onToggleStageSheet(visible: Boolean) {}
        override fun onSelectEducationalYear(year: LookupResponse) {}
        override fun onToggleYearSheet(visible: Boolean) {}
        override fun onToggleFatherDeceased(deceased: Boolean) {}
        override fun onFatherPhoneChange(value: String) {}
        override fun onFatherWhatsappChange(value: String) {}
        override fun onToggleMotherDeceased(deceased: Boolean) {}
        override fun onMotherPhoneChange(value: String) {}
        override fun onMotherWhatsappChange(value: String) {}

        override fun onToggleServantStageSelection(stage: LookupResponse) {}
        override fun onToggleServantStageSheet(visible: Boolean) {}
        override fun onToggleServantYearSelection(year: LookupResponse) {}
        override fun onToggleServantYearSheet(visible: Boolean) {}
        override fun onToggleCanApproveNewRequests(canApprove: Boolean) {}
        override fun onToggleResponsibleStageSelection(stage: LookupResponse) {}
        override fun onToggleResponsibleStageSheet(visible: Boolean) {}
        override fun onToggleResponsibleYearSelection(year: LookupResponse) {}
        override fun onToggleResponsibleYearSheet(visible: Boolean) {}

        override fun onPartnerQueryChange(query: String) {}
        override fun onSearchPartner() {}
        override fun onRemovePartner() {}
        override fun onChildQueryChange(query: String) {}
        override fun onSearchChild() {}
        override fun onRemoveChild(child: UserSummary) {}

        override fun onToggleEducationalStageSelection(stage: LookupResponse) {}
        override fun onToggleStagesSheet(visible: Boolean) {}
        override fun onLoadNextEducationalStages() {}
        override fun onLoadNextRanks() {}

        override fun onNotesChanged(value: String) {}
    }

    Preview(darkTheme = Theme.isDarkTheme) {
        ReviewAndEditRequestContent(
            state = state,
            listener = listener
        )
    }
}
