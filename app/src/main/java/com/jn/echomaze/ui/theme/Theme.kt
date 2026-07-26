package com.jn.echomaze.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onPrimary = DeepSpace,
    onSecondary = DeepSpace,
    onTertiary = DeepSpace,
    onBackground = CyberCyan,
    onSurface = CyberCyan
)

@Composable
fun AppTheme(
    darkTheme: Boolean = true, // Always default to dark for neon aesthetic
    content: @Composable () -> Unit
) {
    // We strictly use our custom DarkColorScheme for the neon look
    val colorScheme = DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Since we are always in dark mode, we want light status bar icons
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
