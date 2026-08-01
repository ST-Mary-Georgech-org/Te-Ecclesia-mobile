package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.lookups.domain.model.LookupResponse

@Composable
fun EducationalStageFields(
    selectedStage: LookupResponse?,
    onToggleStageSheet: (Boolean) -> Unit,
    isStageSheetVisible: Boolean,
    educationalStages: List<LookupResponse>,
    onSelectEducationalStage: (LookupResponse) -> Unit,
    onLoadNextEducationalStages: () -> Unit,
    stageError: String?,
    selectedYear: LookupResponse?,
    onToggleYearSheet: (Boolean) -> Unit,
    isYearSheetVisible: Boolean,
    onSelectEducationalYear: (LookupResponse) -> Unit,
    yearError: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EducationalStageSelectField(
            selectedStage = selectedStage,
            educationalStages = educationalStages,
            isSheetVisible = isStageSheetVisible,
            onToggleSheet = onToggleStageSheet,
            onSelectStage = onSelectEducationalStage,
            onLoadNextStages = onLoadNextEducationalStages,
            errorText = stageError
        )

        val hasYears = !selectedStage?.subItems.isNullOrEmpty() || selectedYear != null

        AnimatedVisibility(
            visible = hasYears,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            EducationalYearSelectField(
                selectedYear = selectedYear,
                availableYears = selectedStage?.subItems ?: emptyList(),
                isSheetVisible = isYearSheetVisible,
                onToggleSheet = onToggleYearSheet,
                onSelectYear = onSelectEducationalYear,
                errorText = yearError
            )
        }
    }
}

@Preview
@Composable
private fun EducationalStageFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            EducationalStageFields(
                selectedStage = null,
                onToggleStageSheet = {},
                isStageSheetVisible = false,
                educationalStages = emptyList(),
                onSelectEducationalStage = {},
                onLoadNextEducationalStages = {},
                stageError = null,
                selectedYear = null,
                onToggleYearSheet = {},
                isYearSheetVisible = false,
                onSelectEducationalYear = {},
                yearError = null
            )
        }
    }
}
