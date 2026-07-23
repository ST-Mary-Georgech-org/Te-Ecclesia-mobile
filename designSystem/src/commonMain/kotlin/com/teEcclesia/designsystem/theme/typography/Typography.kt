package com.teEcclesia.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalTypography =
    staticCompositionLocalOf<Typography> { error("No Typography provided") }
