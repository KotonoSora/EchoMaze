package com.jn.echomaze.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

val DeepSpace = Color(0xFF050510)
val CyberCyan = Color(0xFF00FFFF)
val NeonBlue = CyberCyan
val NeonPurple = Color(0xFFBB00FF)
val ElectricBlue = Color(0xFF007FFF)
val GoldCoin = Color(0xFFFFD700)
val SurfaceDark = Color(0xFF12122A)

val PrimaryDark = CyberCyan
val SecondaryDark = NeonPurple
val TertiaryDark = ElectricBlue

val NeonPink = Color(0xFFFF00FF)
val NeonGreen = Color(0xFF39FF14)
val NeonOrange = Color(0xFFFF5F1F)
val NeonYellow = Color(0xFFFFF01F)

val BackgroundDark = DeepSpace
val SurfaceVariantDark = Color(0xFF1A1A3A)

fun Color.softNeon(): Color = lerp(this, Color.White, 0.7f)

/**
 * Ensures that this color has enough contrast against the provided background color.
 * If the contrast ratio is below the target, it will be lightened towards white.
 */
fun Color.ensureContrast(background: Color = DeepSpace, targetRatio: Float = 4.5f): Color {
    val bgLuminance = background.luminance()
    var currentText = this

    repeat(10) {
        val textLuminance = currentText.luminance()
        val contrast = if (textLuminance > bgLuminance) {
            (textLuminance + 0.05f) / (bgLuminance + 0.05f)
        } else {
            (bgLuminance + 0.05f) / (textLuminance + 0.05f)
        }

        if (contrast >= targetRatio) return currentText
        currentText = lerp(currentText, Color.White, 0.15f)
    }

    return currentText
}
