package com.teEcclesia.identity.presentation.screen.requests.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.sheet.BottomSheet
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.theme.theme.Theme
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.date_asc
import teecclesia.designsystem.generated.resources.date_desc
import teecclesia.designsystem.generated.resources.name_asc
import teecclesia.designsystem.generated.resources.name_desc
import teecclesia.designsystem.generated.resources.sort_by

@Composable
fun SortingOptionBottomSheet(
    isVisible: Boolean,
    currentSortBy: String,
    currentSortOrder: String,
    onSortOptionSelected: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    BottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        containerColor = Theme.colorScheme.surface
    ) {
        Text(
            text = stringResource(Res.string.sort_by),
            style = Theme.typography.titleMedium,
            color = Theme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SortOptionRow(
            text = stringResource(Res.string.date_desc),
            isSelected = currentSortBy == "createdAt" && currentSortOrder == "DESC",
            onClick = { onSortOptionSelected("createdAt", "DESC") }
        )

        SortOptionRow(
            text = stringResource(Res.string.date_asc),
            isSelected = currentSortBy == "createdAt" && currentSortOrder == "ASC",
            onClick = { onSortOptionSelected("createdAt", "ASC") }
        )

        SortOptionRow(
            text = stringResource(Res.string.name_asc),
            isSelected = currentSortBy == "firstName" && currentSortOrder == "ASC",
            onClick = { onSortOptionSelected("firstName", "ASC") }
        )

        SortOptionRow(
            text = stringResource(Res.string.name_desc),
            isSelected = currentSortBy == "firstName" && currentSortOrder == "DESC",
            onClick = { onSortOptionSelected("firstName", "DESC") }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SortOptionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = Theme.colorScheme.primary,
                unselectedColor = Theme.colorScheme.outline
            )
        )
        Text(
            text = text,
            style = Theme.typography.bodyMedium,
            color = Theme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
