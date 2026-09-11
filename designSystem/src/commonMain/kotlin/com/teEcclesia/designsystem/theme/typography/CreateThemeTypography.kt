package com.teEcclesia.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.cairo_bold
import teecclesia.designsystem.generated.resources.cairo_medium
import teecclesia.designsystem.generated.resources.cairo_regular
import teecclesia.designsystem.generated.resources.cairo_semi_bold

@Composable
fun createThemeTypography(): Typography {
    val regular = Font(resource = Res.font.cairo_regular, FontWeight.Normal)
    val medium = Font(resource = Res.font.cairo_medium, FontWeight.Medium)
    val semiBold = Font(resource = Res.font.cairo_semi_bold, FontWeight.SemiBold)
    val bold = Font(resource = Res.font.cairo_bold, FontWeight.Bold)

    val fontFamily = remember(regular, medium, semiBold, bold) {
        FontFamily(regular, medium, semiBold, bold)
    }

    return remember(fontFamily) {
        val defaultTypography = Typography()
        Typography(
            displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
            labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
        )
    }
}
