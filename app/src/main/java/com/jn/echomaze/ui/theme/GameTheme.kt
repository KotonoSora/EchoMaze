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

        val SunsetTheme = GameTheme(
            id = "skin_sunset",
            name = "SUNSET",
            primaryColor = NeonPurple,
            glowColor = NeonPurple,
            tileColor = Color(0xFF2D1B36),
            textColor = Color.White
        )

        val EmeraldTheme = GameTheme(
            id = "skin_emerald",
            name = "EMERALD",
            primaryColor = Color(0xFF00FF88),
            glowColor = Color(0xFF00FF88),
            tileColor = Color(0xFF002211),
            textColor = Color.White
        )

        fun getThemeById(id: String): GameTheme {
            return when (id) {
                "skin_sunset" -> SunsetTheme
                "skin_emerald" -> EmeraldTheme
                else -> NeonBlueTheme
            }
        }
    }
}
