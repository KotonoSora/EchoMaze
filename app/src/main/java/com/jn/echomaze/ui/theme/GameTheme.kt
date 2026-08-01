package com.jn.echomaze.ui.theme

import androidx.compose.ui.graphics.Color

data class GameTheme(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val glowColor: Color,
    val tileColor: Color,
    val textColor: Color
) {
    companion object {
        val NeonBlueTheme = GameTheme(
            id = "skin_neon",
            name = "NEON BLUE",
            primaryColor = CyberCyan,
            glowColor = CyberCyan,
            tileColor = Color.Black,
            textColor = Color.White
        )
    }
}
