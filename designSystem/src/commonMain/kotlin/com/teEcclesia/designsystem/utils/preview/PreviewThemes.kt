package com.teEcclesia.designsystem.utils.preview

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Mode",
    uiMode = UI_MODE_NIGHT_NO,
    group = "Themes",
    heightDp = 1500
)
@Preview(
    name = "Dark Mode",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    group = "Themes",
    heightDp = 1500
)
annotation class PreviewThemes
