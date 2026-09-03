package com.jn.echomaze.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Gameplay : Screen("gameplay")
    data object Pause : Screen("pause")
    data object GameOver : Screen("game_over")
    data object Victory : Screen("victory")
    data object Shop : Screen("shop")
    data object Settings : Screen("settings")
    data object Help : Screen("help")
    data object Leaderboard : Screen("leaderboard")
    data object DailyChallenge : Screen("daily_challenge")
}
