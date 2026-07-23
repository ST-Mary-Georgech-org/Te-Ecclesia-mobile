package com.teEcclesia.designsystem.utils

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",
    group = "devices",
    widthDp = 360,
    heightDp = 640
)

@Preview(
    name = "Arabic",
    locale = "ar",
    group = "devices",
    widthDp = 360,
    heightDp = 640
)

@Preview(
    name = "Tablet",
    group = "devices",
    widthDp = 1280,
    heightDp = 800
)

annotation class PreviewMultiDevices
