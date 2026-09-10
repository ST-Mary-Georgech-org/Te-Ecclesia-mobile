package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import com.teEcclesia.identity.domain.model.UserSummary
import com.teEcclesia.identity.presentation.screen.register.components.UserChip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.ic_plus
import teecclesia.designsystem.generated.resources.search_child
import teecclesia.designsystem.generated.resources.search_child_support_text

@Composable
fun ChildrenSelectionFields(
    childQuery: String,
    onChildQueryChange: (String) -> Unit,
    onSearchChild: () -> Unit,
    selectedChildren: List<UserSummary>,
    onRemoveChild: (UserSummary) -> Unit,
    isLoading: Boolean = false,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomTextField(
            value = childQuery,
            onValueChange = onChildQueryChange,
            labelText = stringResource(Res.string.search_child),
            supportingText = stringResource(Res.string.search_child_support_text),
            errorText = errorText,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = if (childQuery.isNotBlank()) painterResource(Res.drawable.ic_plus) else null,
            onTrailingIconClick = if (childQuery.isNotBlank()) onSearchChild else null,
            isLoading = isLoading,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedChildren.forEach { child ->
                UserChip(
                    user = child,
                    onRemove = { onRemoveChild(child) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChildrenSelectionFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            ChildrenSelectionFields(
                childQuery = "",
                onChildQueryChange = {},
                onSearchChild = {},
                selectedChildren = emptyList(),
                onRemoveChild = {}
            )
        }
    }
}
