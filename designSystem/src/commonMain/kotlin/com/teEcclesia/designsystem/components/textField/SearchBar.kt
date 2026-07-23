package com.teEcclesia.designsystem.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.modifier.clickableNoRipple
import com.teEcclesia.designsystem.theme.theme.Theme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: Painter? = null,
    onTrailingIconClick: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                style = Theme.typography.bodyMedium,
                color = Theme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon?.let { painter ->
            {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = Theme.colorScheme.onSurfaceVariant,
                    modifier = if (onTrailingIconClick != null) {
                        Modifier.clickableNoRipple(onClick = onTrailingIconClick)
                    } else {
                        Modifier
                    }
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}
