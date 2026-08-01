package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.EducationalStageSelectField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.educational_stages
import teecclesia.designsystem.generated.resources.ic_ordination
import teecclesia.designsystem.generated.resources.priest_info

@Composable
fun ReviewStep2KahenContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.priest_info),
            icon = painterResource(Res.drawable.ic_ordination)
        ) {
            EducationalStageSelectField(
                selectedStages = state.kahenEducationalStages,
                educationalStages = state.educationalStages,
                isSheetVisible = state.isStagesSheetVisible,
                onToggleSheet = listener::onToggleStagesSheet,
                onSelectStage = listener::onToggleEducationalStageSelection,
                label = stringResource(Res.string.educational_stages),
                onLoadNextStages = listener::onLoadNextEducationalStages
            )
        }
    }
}
