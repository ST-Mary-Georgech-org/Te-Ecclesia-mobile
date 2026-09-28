package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.ChildrenSelectionFields
import com.teEcclesia.identity.presentation.shared.components.OrdinationInfoFields
import com.teEcclesia.identity.presentation.shared.components.PartnerSelectionFields
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.children
import teecclesia.designsystem.generated.resources.file_ordination_certificate
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.ic_ordination
import teecclesia.designsystem.generated.resources.ordination_certificate_optional
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.partner

@Composable
fun ReviewStep2ParentContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
    onFileClickOrdinationCertificate: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.partner),
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

        if (state.isMale != false) {
            ReviewSectionCard(
                title = stringResource(Res.string.ordination_info),
                icon = painterResource(Res.drawable.ic_ordination)
            ) {
                OrdinationInfoFields(
                    isOrdained = state.isOrdained,
                    onToggleOrdained = listener::onToggleOrdained,
                    selectedRank = state.selectedRank,
                    ranks = state.ranks,
                    isRankSheetVisible = state.isRankSheetVisible,
                    onToggleRankSheet = listener::onToggleRankSheet,
                    onSelectRank = listener::onSelectRank,
                    rankError = state.rankError?.asString(),
                    isRankLoading = state.isRankLoading,
                    isRankLoadFailed = state.isRankLoadFailed,
                    onRetryLoadRanks = listener::onRetryLoadRanks,
                    isOrdainedInThisChurch = state.isOrdainedInThisChurch,
                    onToggleOrdainedInThisChurch = listener::onToggleOrdainedInThisChurch,
                    ordinationYear = state.ordinationYear,
                    onOrdinationYearChange = listener::onOrdinationYearChange,
                    ordinationYearError = state.ordinationYearError?.asString(),
                    bishopName = state.bishopName,
                    onBishopNameChange = listener::onBishopNameChange,
                    bishopNameError = state.bishopNameError?.asString(),
                    ordinationPlace = state.ordinationPlace,
                    onOrdinationPlaceChange = listener::onOrdinationPlaceChange,
                    ordinationPlaceError = state.ordinationPlaceError?.asString(),
                    filePickerContent = {
                        ReviewFilePickerRow(
                            label = stringResource(Res.string.ordination_certificate_optional),
                            fileTitle = stringResource(Res.string.file_ordination_certificate),
                            fileName = state.ordinationCertificateFileName,
                            fileBytes = state.ordinationCertificateBytes,
                            onUploadClick = { listener.onClickUpload(UploadTarget.ORDINATION_CERTIFICATE) },
                            onClearClick = {
                                listener.onSelectImageBytes(UploadTarget.ORDINATION_CERTIFICATE, null, null)
                            },
                            onFileClick = onFileClickOrdinationCertificate
                        )
                    }
                )
            }
        }
    }
}
