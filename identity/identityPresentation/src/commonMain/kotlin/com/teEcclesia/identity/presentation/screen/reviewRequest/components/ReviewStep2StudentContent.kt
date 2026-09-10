package com.teEcclesia.identity.presentation.screen.reviewRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.utils.asString
import com.teEcclesia.identity.presentation.screen.register.UploadTarget
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestInteractionListener
import com.teEcclesia.identity.presentation.screen.reviewRequest.ReviewAndEditRequestUiState
import com.teEcclesia.identity.presentation.shared.components.DeaconSchoolFields
import com.teEcclesia.identity.presentation.shared.components.EducationalStageFields
import com.teEcclesia.identity.presentation.shared.components.OrdinationInfoFields
import com.teEcclesia.identity.presentation.shared.components.ParentsContactFields
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.deacons_school_details
import teecclesia.designsystem.generated.resources.ic_family
import teecclesia.designsystem.generated.resources.ic_ordination
import teecclesia.designsystem.generated.resources.ic_school
import teecclesia.designsystem.generated.resources.file_ordination_certificate
import teecclesia.designsystem.generated.resources.file_identity_card
import teecclesia.designsystem.generated.resources.identity_card_certificate_optional
import teecclesia.designsystem.generated.resources.ordination_certificate_optional
import teecclesia.designsystem.generated.resources.ordination_info
import teecclesia.designsystem.generated.resources.personal_info

@Composable
fun ReviewStep2StudentContent(
    state: ReviewAndEditRequestUiState,
    listener: ReviewAndEditRequestInteractionListener,
    modifier: Modifier = Modifier,
    onFileClickOrdinationCertificate: (() -> Unit)? = null,
    onFileClickIdentityCertificate: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewSectionCard(
            title = stringResource(Res.string.deacons_school_details),
            icon = painterResource(Res.drawable.ic_school)
        ) {
            DeaconSchoolFields(
                shamamsaStatus = state.shamamsaStatus,
                onShamamsaStatusSelected = listener::onShamamsaStatusSelected
            )
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
                    ordinationPlace = state.ordinationPlace,
                    onOrdinationPlaceChange = listener::onOrdinationPlaceChange,
                    filePickerContent = {
                        ReviewFilePickerRow(
                            label = stringResource(Res.string.ordination_certificate_optional),
                            fileTitle = stringResource(Res.string.file_ordination_certificate),
                            fileName = state.ordinationCertificateFileName,
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

        ReviewSectionCard(
            title = stringResource(Res.string.personal_info),
            icon = painterResource(Res.drawable.ic_family)
        ) {
            EducationalStageFields(
                selectedStage = state.studentEducationalStage,
                onToggleStageSheet = listener::onToggleStageSheet,
                isStageSheetVisible = state.isStageSheetVisible,
                educationalStages = state.educationalStages,
                isStageLoading = state.isStageLoading,
                isStageLoadFailed = state.isStageLoadFailed,
                onRetryLoadStages = listener::onRetryLoadEducationalStages,
                onSelectEducationalStage = listener::onSelectEducationalStage,
                onLoadNextEducationalStages = listener::onLoadNextEducationalStages,
                stageError = state.stageError?.asString(),
                selectedYear = state.studentEducationalYear,
                onToggleYearSheet = listener::onToggleYearSheet,
                isYearSheetVisible = state.isYearSheetVisible,
                onSelectEducationalYear = listener::onSelectEducationalYear,
                yearError = state.yearError?.asString()
            )

            ParentsContactFields(
                isFatherDeceased = state.isFatherDeceased,
                onToggleFatherDeceased = listener::onToggleFatherDeceased,
                fatherPhone = state.fatherPhone,
                onFatherPhoneChange = listener::onFatherPhoneChange,
                fatherPhoneError = state.fatherPhoneError?.asString(),
                fatherWhatsapp = state.fatherWhatsapp,
                onFatherWhatsappChange = listener::onFatherWhatsappChange,
                fatherWhatsappError = state.fatherWhatsappError?.asString(),
                isFatherWhatsappSameAsPhone = state.isFatherWhatsappSameAsPhone,
                onToggleFatherWhatsappSameAsPhone = listener::onToggleFatherWhatsappSameAsPhone,
                isMotherDeceased = state.isMotherDeceased,
                onToggleMotherDeceased = listener::onToggleMotherDeceased,
                motherPhone = state.motherPhone,
                onMotherPhoneChange = listener::onMotherPhoneChange,
                motherPhoneError = state.motherPhoneError?.asString(),
                motherWhatsapp = state.motherWhatsapp,
                onMotherWhatsappChange = listener::onMotherWhatsappChange,
                motherWhatsappError = state.motherWhatsappError?.asString(),
                isMotherWhatsappSameAsPhone = state.isMotherWhatsappSameAsPhone,
                onToggleMotherWhatsappSameAsPhone = listener::onToggleMotherWhatsappSameAsPhone,
                filePickerContent = {
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
            )
        }
    }
}
