package com.teEcclesia.identity.presentation.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teEcclesia.designsystem.components.textField.CustomTextField
import com.teEcclesia.designsystem.theme.theme.Theme
import com.teEcclesia.designsystem.utils.Preview
import org.jetbrains.compose.resources.stringResource
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.apartment
import teecclesia.designsystem.generated.resources.branching_from
import teecclesia.designsystem.generated.resources.building_no
import teecclesia.designsystem.generated.resources.floor
import teecclesia.designsystem.generated.resources.special_mark
import teecclesia.designsystem.generated.resources.street

@Composable
fun AddressFieldsSection(
    buildingNo: String,
    onBuildingNoChange: (String) -> Unit,
    buildingNoError: String?,
    street: String,
    onStreetChange: (String) -> Unit,
    streetError: String?,
    streetBranch: String,
    onStreetBranchChange: (String) -> Unit,
    area: String,
    onAreaChange: (String) -> Unit,
    areas: List<String>,
    isAreaSheetVisible: Boolean,
    onToggleAreaSheet: (Boolean) -> Unit,
    onSelectArea: (String) -> Unit,
    areaError: String?,
    floor: String,
    onFloorChange: (String) -> Unit,
    floorError: String?,
    apartment: String,
    onApartmentChange: (String) -> Unit,
    specialMark: String,
    onSpecialMarkChange: (String) -> Unit,
    specialMarkError: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = buildingNo,
            onValueChange = onBuildingNoChange,
            labelText = stringResource(Res.string.building_no),
            modifier = Modifier.fillMaxWidth(),
            errorText = buildingNoError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true
        )

        CustomTextField(
            value = street,
            onValueChange = onStreetChange,
            labelText = stringResource(Res.string.street),
            modifier = Modifier.fillMaxWidth(),
            errorText = streetError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true
        )

        CustomTextField(
            value = streetBranch,
            onValueChange = onStreetBranchChange,
            labelText = stringResource(Res.string.branching_from),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            singleLine = true
        )

        AreaDropdownField(
            value = area,
            onValueChange = onAreaChange,
            areas = areas,
            isAreaSheetVisible = isAreaSheetVisible,
            onToggleAreaSheet = onToggleAreaSheet,
            onSelectArea = onSelectArea,
            errorText = areaError
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomTextField(
                value = floor,
                onValueChange = onFloorChange,
                labelText = stringResource(Res.string.floor),
                modifier = Modifier.weight(1f),
                errorText = floorError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )

            CustomTextField(
                value = apartment,
                onValueChange = onApartmentChange,
                labelText = stringResource(Res.string.apartment),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                singleLine = true
            )
        }

        CustomTextField(
            value = specialMark,
            onValueChange = onSpecialMarkChange,
            labelText = stringResource(Res.string.special_mark),
            modifier = Modifier.fillMaxWidth(),
            errorText = specialMarkError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            singleLine = true
        )
    }
}

@Preview
@Composable
private fun AddressFieldsSectionPreview() {
    Theme(darkTheme = Theme.isDarkTheme) {
        Preview(darkTheme = Theme.isDarkTheme) {
            AddressFieldsSection(
                buildingNo = "12",
                onBuildingNoChange = {},
                buildingNoError = null,
                street = "Shubra",
                onStreetChange = {},
                streetError = null,
                streetBranch = "",
                onStreetBranchChange = {},
                area = "Shubra",
                onAreaChange = {},
                areas = listOf("Shubra"),
                isAreaSheetVisible = false,
                onToggleAreaSheet = {},
                onSelectArea = {},
                areaError = null,
                floor = "3",
                onFloorChange = {},
                floorError = null,
                apartment = "12",
                onApartmentChange = {},
                specialMark = "Near church",
                onSpecialMarkChange = {},
                specialMarkError = null
            )
        }
    }
}
