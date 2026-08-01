package com.teEcclesia.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import teecclesia.designsystem.generated.resources.Res
import teecclesia.designsystem.generated.resources.poppins_medium
import teecclesia.designsystem.generated.resources.poppins_regular
import teecclesia.designsystem.generated.resources.poppins_semi_bold

import androidx.compose.runtime.remember

@Composable
fun createThemeTypography(): Typography {
    val regular = Font(resource = Res.font.poppins_regular, FontWeight.Normal)
    val medium = Font(resource = Res.font.poppins_medium, FontWeight.Medium)
    val semiBold = Font(resource = Res.font.poppins_semi_bold, FontWeight.SemiBold)

    val poppinsFontFamily = remember(regular, medium, semiBold) {
        FontFamily(regular, medium, semiBold)
    }

    return remember(poppinsFontFamily) {
        val defaultTypography = Typography()
        Typography(
            displayLarge = defaultTypography.displayLarge.copy(fontFamily = poppinsFontFamily),
            displayMedium = defaultTypography.displayMedium.copy(fontFamily = poppinsFontFamily),
            displaySmall = defaultTypography.displaySmall.copy(fontFamily = poppinsFontFamily),
            headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = poppinsFontFamily),
            headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = poppinsFontFamily),
            headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = poppinsFontFamily),
            titleLarge = defaultTypography.titleLarge.copy(fontFamily = poppinsFontFamily),
            titleMedium = defaultTypography.titleMedium.copy(fontFamily = poppinsFontFamily),
            titleSmall = defaultTypography.titleSmall.copy(fontFamily = poppinsFontFamily),
            bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = poppinsFontFamily),
            bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = poppinsFontFamily),
            bodySmall = defaultTypography.bodySmall.copy(fontFamily = poppinsFontFamily),
            labelLarge = defaultTypography.labelLarge.copy(fontFamily = poppinsFontFamily),
            labelMedium = defaultTypography.labelMedium.copy(fontFamily = poppinsFontFamily),
            labelSmall = defaultTypography.labelSmall.copy(fontFamily = poppinsFontFamily)
        )
    }
}
