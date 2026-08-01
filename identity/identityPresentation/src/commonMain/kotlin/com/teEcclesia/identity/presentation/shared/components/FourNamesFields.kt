package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.text.Text
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.first_name
import teecclesia.designsystem.generated.resources.full_name_in_arabic
import teecclesia.designsystem.generated.resources.last_name
import teecclesia.designsystem.generated.resources.second_name
import teecclesia.designsystem.generated.resources.third_name

@Composable
fun FourNamesFields(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    firstNameError: String?,
    secondName: String,
    onSecondNameChange: (String) -> Unit,
    secondNameError: String?,
    thirdName: String,
    onThirdNameChange: (String) -> Unit,
    thirdNameError: String?,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    lastNameError: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                labelText = stringResource(Res.string.first_name),
                modifier = Modifier.weight(1f),
                errorText = firstNameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            CustomTextField(
                value = secondName,
                onValueChange = onSecondNameChange,
                labelText = stringResource(Res.string.second_name),
                modifier = Modifier.weight(1f),
                errorText = secondNameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                value = thirdName,
                onValueChange = onThirdNameChange,
                labelText = stringResource(Res.string.third_name),
                modifier = Modifier.weight(1f),
                errorText = thirdNameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            CustomTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                labelText = stringResource(Res.string.last_name),
                modifier = Modifier.weight(1f),
                errorText = lastNameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }
    }
}

@Preview
@Composable
private fun FourNamesFieldsPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            FourNamesFields(
                firstName = "مينا",
                onFirstNameChange = {},
                firstNameError = null,
                secondName = "عادل",
                onSecondNameChange = {},
                secondNameError = null,
                thirdName = "فايز",
                onThirdNameChange = {},
                thirdNameError = null,
                lastName = "رمزي",
                onLastNameChange = {},
                lastNameError = null
            )
        }
    }
}
