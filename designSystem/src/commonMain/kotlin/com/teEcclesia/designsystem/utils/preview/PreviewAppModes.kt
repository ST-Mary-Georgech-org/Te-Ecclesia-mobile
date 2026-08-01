package com.teEcclesia.designsystem.utils.preview

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "English - Light",
    locale = "en",
    uiMode = UI_MODE_NIGHT_NO,
    group = "App State"
)
@Preview(
    name = "English - Dark",
    locale = "en",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    group = "App State"
)
@Preview(
    name = "Arabic - Light",
    locale = "ar",
    uiMode = UI_MODE_NIGHT_NO,
    group = "App State"
)
@Preview(
    name = "Arabic - Dark",
    locale = "ar",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    group = "App State"
)
annotation class PreviewAppModes