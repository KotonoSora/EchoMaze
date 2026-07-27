package com.jn.echomaze.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object LevelSelect : Screen("level_select")
    data object Gameplay : Screen("gameplay/{levelId}") {
        fun createRoute(levelId: Int) = "gameplay/$levelId"
    }

    data object Pause : Screen("pause")
    data object LevelComplete : Screen("level_complete/{levelId}/{score}/{coins}/{stars}/{time}") {
        fun createRoute(levelId: Int, score: Int, coins: Int, stars: Int, time: Int) =
            "level_complete/$levelId/$score/$coins/$stars/$time"
    }

    data object GameOver : Screen("game_over")
    data object Shop : Screen("shop")
    data object Settings : Screen("settings")
    data object Leaderboard : Screen("leaderboard")
    data object DailyChallenge : Screen("daily_challenge")
    data object Help : Screen("help")
    data object SkinsShop : Screen("skins_shop")
}
