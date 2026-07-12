package com.teEcclesia.designsystem.theme.color.scheme

import androidx.compose.ui.graphics.Color

internal val LightColorScheme = ColorScheme(
    text = ColorScheme.Text(
        body = Color(0xFF8193B1),
        bodyBlue = Color(0xFF69C7FD),
        headingBlue = Color(0xFF179FDD),
        label = Color(0xFF8193B1),
        link = Color(0xFFC0C9D8),
        title = Color(0xFF2D3748),
        titleSmall = Color(0xFF4E607E)
    ),
    additional = ColorScheme.Additional(
        blue = Color(0xFF9ED9FD),
        error = Color(0xFFF8D3D3),
        onError = Color(0xFFDC2626),
        onSuccess = Color(0xFF00CC7A),
        onDone = Color(0xFF00CC7A),
        onWarning = Color(0xFFEA5A0C),
        pink = Color(0xFFEC4899),
        purple = Color(0xFF9A83CE),
        success = Color(0xFFCCFFEB),
        warning = Color(0xFFFDDFCE),
        done = Color(0xFF4E607E)
    ),
    background = ColorScheme.Background(
        octonary = Color(0xFF179FDD).copy(alpha = 0.7f),
        quaternary = Color(0xFFF8FBFC),
        quinary = Color(0xFFF8FBFC),
        senary = Color(0xFF179FDD).copy(alpha = 0.08f),
        septenary = Color(0xFF179FDD).copy(alpha = 0.4f),
        primary = Color(0xFFF8FBFC),
        secondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFFFFFFFF)
    ),
    border = ColorScheme.Border(
        active = Color(0xFF964D0D),
        primary = Color(0xFFF1F5F9),
        secondary = Color(0xFF1F1F1F).copy(alpha = 0.1f),
        tertiary = Color(0xFFE0E4EB)
    ),
    brand = ColorScheme.Brand(
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFF8193B1),
        primary = Color(0xFF964D0D),
        primaryGradient1 = Color(0xFF00EEFF),
        primaryGradient2 = Color(0xFF179FDD),
        primaryVariant = Color(0xFF0F7BAE),
        secondary = Color(0xFFFFFFFF),
        secondaryVariant = Color(0xFFA1AEC4)
    ),
    button = ColorScheme.Button(
        onPrimary = Color(0xFFFFFFFF),
        onQuaternary = Color(0xFFFFFFFF),
        onQuinary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFF8193B1),
        onTertiary = Color(0xFF179FDD),
        primary = Color(0xFF964D0D),
        quaternary = Color(0xFF179FDD),
        secondary = Color(0xFFF0F9FF),
        quinary = Color(0xFF179FDD),
        tertiary = Color(0xFFF0F9FF)
    ),
    icon = ColorScheme.Icon(
        primary = Color(0xFF179FDD),
        secondary = Color(0xFF179FDD),
        tertiary = Color(0xFF82C7FF).copy(alpha = 0.4f)
    ),
    primary = ColorScheme.PrimaryVariant(
        variant50 = Color(0xFFF0F9FF),
        variant100 = Color(0xFFE0F2FE),
        variant200 = Color(0xFFC5E8FE),
        variant300 = Color(0xFF9ED9FD),
        variant400 = Color(0xFF69C7FD),
        variant500 = Color(0xFF2BB9FC),
        variant600 = Color(0xFF179FDD),
        variant700 = Color(0xFF0F7BAE),
        variant800 = Color(0xFF0A608A),
        variant900 = Color(0xFF064E71),
        variant950 = Color(0xFF022C43)
    )
)
