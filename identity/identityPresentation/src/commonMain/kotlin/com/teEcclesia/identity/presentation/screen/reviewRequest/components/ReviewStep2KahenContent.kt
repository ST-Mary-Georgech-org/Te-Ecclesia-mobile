package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.sheet.EducationalStageSelectField
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.ChildrenSelectionFields
import com.teEcclesia.identity.presentation.shared.components.PartnerSelectionFields
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.children
import teecclesia.designsystem.generated.resources.educational_stages
import teecclesia.designsystem.generated.resources.family_information
import teecclesia.designsystem.generated.resources.i_am_also_a_parent
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.ic_ordination
import teecclesia.designsystem.generated.resources.partner
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
                itemTitle = { it.name },
                itemId = { it.id },
                isSelected = { stage -> state.kahenEducationalStages.any { it.id == stage.id } },
                isSheetVisible = state.isStagesSheetVisible,
                onToggleSheet = listener::onToggleStagesSheet,
                onSelectStage = listener::onToggleEducationalStageSelection,
                label = stringResource(Res.string.educational_stages),
                onLoadNextStages = listener::onLoadNextEducationalStages,
                isStageLoading = state.isStageLoading,
                isStageLoadFailed = state.isStageLoadFailed,
                onRetryLoadStages = listener::onRetryLoadEducationalStages
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = state.isAlsoParent,
                onCheckedChange = listener::onToggleAlsoParent
            )
            Text(
                text = stringResource(Res.string.i_am_also_a_parent),
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurface,
                modifier = Modifier.clickableNoRipple { listener.onToggleAlsoParent(!state.isAlsoParent) }
            )
        }

        AnimatedVisibility(
            visible = state.isAlsoParent,
            modifier = Modifier.fillMaxWidth()
        ) {
            ReviewSectionCard(
                title = stringResource(Res.string.family_information),
                icon = painterResource(Res.drawable.ic_family)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PartnerSelectionFields(
                        selectedPartner = state.selectedPartner,
                        partnerQuery = state.partnerQuery,
                        onPartnerQueryChange = listener::onPartnerQueryChange,
                        onSearchPartner = listener::onSearchPartner,
                        onRemovePartner = listener::onRemovePartner,
                        isLoading = state.isPartnerLoading,
                        errorText = state.partnerError?.asString()
                    )

                    Text(
                        text = stringResource(Res.string.children),
                        style = Theme.typography.titleMedium,
                        color = Theme.colorScheme.onSurface
                    )

                    ChildrenSelectionFields(
                        childQuery = state.childQuery,
                        onChildQueryChange = listener::onChildQueryChange,
                        onSearchChild = listener::onSearchChild,
                        selectedChildren = state.selectedChildren,
                        onRemoveChild = listener::onRemoveChild,
                        isLoading = state.isChildLoading,
                        errorText = state.childError?.asString()
                    )
                }
            }
        }
    }
}
