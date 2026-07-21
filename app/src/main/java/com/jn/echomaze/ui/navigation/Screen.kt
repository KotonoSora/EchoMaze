package com.jn.echomaze.ui.navigation

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object LevelSelect : Screen("level_select")
    object Gameplay : Screen("gameplay/{levelId}") {
        fun createRoute(levelId: Int) = "gameplay/$levelId"
    }
    object Pause : Screen("pause")
    object LevelComplete : Screen("level_complete/{levelId}/{score}/{coins}") {
        fun createRoute(levelId: Int, score: Int, coins: Int) = "level_complete/$levelId/$score/$coins"
    }
    object GameOver : Screen("game_over")
    object Shop : Screen("shop")
    object Settings : Screen("settings")
}
