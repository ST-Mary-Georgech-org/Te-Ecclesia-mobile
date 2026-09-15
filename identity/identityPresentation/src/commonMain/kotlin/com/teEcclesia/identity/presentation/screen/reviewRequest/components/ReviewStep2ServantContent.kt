package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.checkbox.Checkbox
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.designsystem.components.sheet.EducationalStageSelectField
import com.teEcclesia.identity.presentation.shared.components.EducationalYearSelectField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import teecclesia.designsystem.generated.resources.file_identity_card
import teecclesia.designsystem.generated.resources.identity_card_certificate_optional
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.can_approve_new_requests
import teecclesia.designsystem.generated.resources.educational_stages
import teecclesia.designsystem.generated.resources.educational_years
import teecclesia.designsystem.generated.resources.ic_school
import teecclesia.designsystem.generated.resources.ic_user_settings
import com.teEcclesia.identity.presentation.shared.components.ChildrenSelectionFields
import com.teEcclesia.identity.presentation.shared.components.PartnerSelectionFields
import teecclesia.designsystem.generated.resources.children
import teecclesia.designsystem.generated.resources.family_information
import teecclesia.designsystem.generated.resources.i_am_also_a_parent
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.partner
import teecclesia.designsystem.generated.resources.permissions
import teecclesia.designsystem.generated.resources.responsible_stages_optional
import teecclesia.designsystem.generated.resources.responsible_years_optional
import teecclesia.designsystem.generated.resources.service_info

@Composable
fun ReviewStep2ServantContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.service_info),
            icon = painterResource(Res.drawable.ic_school)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EducationalStageSelectField(
                    selectedStage = state.servantEducationalStage,
                    educationalStages = state.educationalStages,
                    itemTitle = { it.name },
                    itemId = { it.id },
                    isSelected = { it.id == state.servantEducationalStage?.id },
                    isSheetVisible = state.isServantStageSheetVisible,
                    onToggleSheet = listener::onToggleServantStageSheet,
                    onSelectStage = listener::onToggleServantStageSelection,
                    label = stringResource(Res.string.educational_stages),
                    onLoadNextStages = listener::onLoadNextEducationalStages,
                    isStageLoading = state.isStageLoading,
                    isStageLoadFailed = state.isStageLoadFailed,
                    onRetryLoadStages = listener::onRetryLoadEducationalStages
                )

                AnimatedVisibility(
                    visible = state.servantAvailableYears.isNotEmpty(),
                    enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.expandVertically(),
                    exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.shrinkVertically()
                ) {
                    EducationalYearSelectField(
                        selectedYear = state.servantEducationalYear,
                        availableYears = state.servantAvailableYears,
                        isSheetVisible = state.isServantYearSheetVisible,
                        onToggleSheet = listener::onToggleServantYearSheet,
                        onSelectYear = listener::onToggleServantYearSelection,
                        label = stringResource(Res.string.educational_years)
                    )
                }
            }
        }

        ReviewSectionCard(
            title = stringResource(Res.string.permissions),
            icon = painterResource(Res.drawable.ic_user_settings)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableNoRipple {
                            listener.onToggleCanApproveNewRequests(!state.canApproveNewRequests)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = state.canApproveNewRequests,
                        onCheckedChange = listener::onToggleCanApproveNewRequests,
                        checkedColor = Theme.colorScheme.primary,
                        uncheckedColor = Theme.colorScheme.outline
                    )
                    Text(
                        text = stringResource(Res.string.can_approve_new_requests),
                        style = Theme.typography.bodyMedium,
                        color = Theme.colorScheme.onSurface
                    )
                }

                EducationalStageSelectField(
                    selectedStages = state.responsibleStages,
                    educationalStages = state.educationalStages,
                    itemTitle = { it.name },
                    itemId = { it.id },
                    isSelected = { stage -> state.responsibleStages.any { it.id == stage.id } },
                    isSheetVisible = state.isResponsibleStageSheetVisible,
                    onToggleSheet = listener::onToggleResponsibleStageSheet,
                    onSelectStage = listener::onToggleResponsibleStageSelection,
                    label = stringResource(Res.string.responsible_stages_optional),
                    onLoadNextStages = listener::onLoadNextEducationalStages,
                    isStageLoading = state.isStageLoading,
                    isStageLoadFailed = state.isStageLoadFailed,
                    onRetryLoadStages = listener::onRetryLoadEducationalStages
                )

                EducationalYearSelectField(
                    selectedYears = state.responsibleYears,
                    availableYears = state.allAvailableYearsForPermissions,
                    isSheetVisible = state.isResponsibleYearSheetVisible,
                    onToggleSheet = listener::onToggleResponsibleYearSheet,
                    onSelectYear = listener::onToggleResponsibleYearSelection,
                    label = stringResource(Res.string.responsible_years_optional)
                )
            }
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
