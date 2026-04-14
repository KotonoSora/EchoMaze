package com.kotonosora.echomaze.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kotonosora.echomaze.R

// Define the global font family
val PressStart2P = FontFamily(
    Font(R.font.press_start_2p, FontWeight.Normal)
)

private val defaultTypography = Typography()

// Apply the font family to all Material typography styles
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = PressStart2P),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = PressStart2P),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = PressStart2P),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = PressStart2P),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = PressStart2P),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = PressStart2P),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = PressStart2P),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = PressStart2P),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = PressStart2P),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = PressStart2P),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = PressStart2P),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = PressStart2P),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = PressStart2P),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = PressStart2P),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = PressStart2P)
)
