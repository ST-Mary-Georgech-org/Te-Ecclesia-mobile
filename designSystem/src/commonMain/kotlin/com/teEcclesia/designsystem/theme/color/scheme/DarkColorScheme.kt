package com.teEcclesia.designsystem.theme.color.scheme

import androidx.compose.ui.graphics.Color

internal val DarkColorScheme = ColorScheme(
    text = ColorScheme.Text(
        body = Color(0xFF8193B1),
        bodyBlue = Color(0xFF179FDD),
        headingBlue = Color(0xFF9ED9FD),
        label = Color(0xFF4E607E),
        link = Color(0xFF4E607E),
        title = Color(0xFFE2E8F0),
        titleSmall = Color(0xFFA1AEC4)
    ),
    additional = ColorScheme.Additional(
        blue = Color(0xFF179FDD),
        error = Color(0xFF2C0707),
        onError = Color(0xFFE34F4F),
        onSuccess = Color(0xFF00CC7A),
        onDone = Color(0xFF00CC7A),
        onWarning = Color(0xFFF57D3D),
        pink = Color(0xFFEC4899),
        purple = Color(0xFF9A83CE),
        success = Color(0xFF00331F),
        warning = Color(0xFF311302),
        done = Color(0xFF001B2E)
    ),
    background = ColorScheme.Background(
        octonary = Color(0xFF179FDD).copy(alpha = 0.7f),
        quaternary = Color(0xFF01243E),
        quinary = Color(0xFF00121F),
        senary = Color(0xFF179FDD).copy(alpha = 0.08f),
        septenary = Color(0xFF179FDD).copy(alpha = 0.4f),
        primary = Color(0xFF030607),
        secondary = Color(0xFF091114),
        tertiary = Color(0xFF00121F)
    ),
    border = ColorScheme.Border(
        active = Color(0xFF69C7FD),
        primary = Color(0xFF161724),
        secondary = Color(0xFF24263B),
        tertiary = Color(0xFF4E607E)
    ),
    brand = ColorScheme.Brand(
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFF2BB9FC),
        primary = Color(0xFF091114),
        primaryGradient1 = Color(0xFF004E85),
        primaryGradient2 = Color(0xFF179FDD),
        primaryVariant = Color(0xFF022C43),
        secondary = Color(0xFF091114),
        secondaryVariant = Color(0xFF064E71)
    ),
    button = ColorScheme.Button(
        onPrimary = Color(0xFFFFFFFF),
        onQuaternary = Color(0xFF0F7BAE),
        onQuinary = Color(0xFF9ED9FD),
        onSecondary = Color(0xFFC0C9D8),
        onTertiary = Color(0xFFFFFFFF),
        primary = Color(0xFF2BB9FC),
        quaternary = Color(0xFFC5E8FE),
        secondary = Color(0xFF001B2E),
        quinary = Color(0xFF001B2E),
        tertiary = Color(0xFF27303F)
    ),
    icon = ColorScheme.Icon(
        primary = Color(0xFF2BB9FC),
        secondary = Color(0xFF064E71),
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