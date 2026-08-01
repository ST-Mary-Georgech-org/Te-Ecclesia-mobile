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
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.ChildrenSelectionFields
import com.teEcclesia.identity.presentation.shared.components.PartnerSelectionFields
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.children
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.file_identity_card
import teecclesia.designsystem.generated.resources.identity_card_certificate_optional
import teecclesia.designsystem.generated.resources.partner

@Composable
fun ReviewStep2ParentContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
    onFileClickIdentityCertificate: (() -> Unit)? = null
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
                    onRemovePartner = listener::onRemovePartner
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
                    onRemoveChild = listener::onRemoveChild
                )

                Spacer(modifier = Modifier.height(4.dp))

                ReviewFilePickerRow(
                    label = stringResource(Res.string.identity_card_certificate_optional),
                    fileTitle = stringResource(Res.string.file_identity_card),
                    fileName = state.identityCertificateFileName,
                    onUploadClick = { listener.onClickUpload(UploadTarget.IDENTITY_CERTIFICATE) },
                    onClearClick = {
                        listener.onSelectImageBytes(UploadTarget.IDENTITY_CERTIFICATE, null, null)
                    },
                    onFileClick = onFileClickIdentityCertificate
                )
            }
        }
    }
}
