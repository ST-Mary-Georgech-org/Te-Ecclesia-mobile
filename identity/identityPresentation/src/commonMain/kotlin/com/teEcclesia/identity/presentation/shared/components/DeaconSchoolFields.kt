package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.radioButton.RadioButton
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.domain.model.ShamamsaStudyStatus
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.a_long_time_ago
import teecclesia.designsystem.generated.resources.have_you_attended_a_deacon_school_before
import teecclesia.designsystem.generated.resources.no
import teecclesia.designsystem.generated.resources.yes

@Composable
fun DeaconSchoolFields(
    shamamsaStatus: ShamamsaStudyStatus,
    onShamamsaStatusSelected: (ShamamsaStudyStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = stringResource(Res.string.have_you_attended_a_deacon_school_before),
                style = Theme.typography.bodyLarge,
                color = Theme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShamamsaStudyStatus.entries.forEach { status ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = shamamsaStatus == status,
                            onClick = { onShamamsaStatusSelected(status) }
                        )
                        Text(
                            stringResource(
                                when (status) {
                                    ShamamsaStudyStatus.YES -> Res.string.yes
                                    ShamamsaStudyStatus.NO -> Res.string.no
                                    ShamamsaStudyStatus.LONG_AGO -> Res.string.a_long_time_ago
                                }
                            ),
                            style = Theme.typography.bodyMedium,
                            color = Theme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeaconSchoolFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            DeaconSchoolFields(
                shamamsaStatus = ShamamsaStudyStatus.NO,
                onShamamsaStatusSelected = {}
            )
        }
    }
}
