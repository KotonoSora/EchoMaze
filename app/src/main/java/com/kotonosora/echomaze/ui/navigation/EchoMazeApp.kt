package com.kotonosora.echomaze.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kotonosora.echomaze.data.AppDatabase
import com.kotonosora.echomaze.ui.screens.*
import com.kotonosora.echomaze.ui.viewmodel.GameViewModel

@Composable
fun EchoMazeApp() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
    val context = LocalContext.current
    val activity = context as Activity
    val database = AppDatabase.getDatabase(context)
    val levels by database.gameDao().getAllLevels().collectAsState(initial = emptyList())
    val coins by database.gameDao().getCoinBalance().collectAsState(initial = null)
    val stats by database.gameDao().getGameStats().collectAsState(initial = null)

    NavHost(navController = navController, startDestination = Screen.MainMenu.route) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                coinBalance = coins?.balance ?: 0,
                onPlayClick = { navController.navigate(Screen.LevelSelect.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onShopClick = { navController.navigate(Screen.Shop.route) },
                onLeaderboardClick = { navController.navigate(Screen.Settings.route) } // Points to Settings/Stats
            )
        }

        composable(Screen.LevelSelect.route) {
            LevelSelectScreen(
                levels = levels,
                onLevelClick = { levelId ->
                    gameViewModel.startLevel(levelId)
                    navController.navigate(Screen.Gameplay.createRoute(levelId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Gameplay.route,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            GameplayScreen(
                levelId = levelId,
                coins = gameViewModel.coinsEarned,
                pulsesRemaining = gameViewModel.pulsesRemaining,
                pulseEngine = gameViewModel.pulseEngine,
                mazeData = gameViewModel.mazeData,
                playerPos = gameViewModel.playerPos,
                onMove = { gameViewModel.handleMove(it) },
                onPauseClick = { navController.navigate(Screen.Pause.route) },
                onPulseClick = { x, y -> gameViewModel.triggerPulse(x, y) },
                onBuyExtraPulses = { gameViewModel.buyExtraPulses() },
                onRevealMap = { gameViewModel.buyRevealMap() },
                onBuyHint = { gameViewModel.buyHintPath() }
            )

            // Navigation to Level Complete is handled via a side effect in the ViewModel
            // or we can observe a state. For now, let's keep it simple.
            // But we need a way to trigger navigation from VM.
            // Let's check if we should add a 'isLevelComplete' state to VM.
        }

        composable(Screen.Pause.route) {
            PauseScreen(
                onResumeClick = { navController.popBackStack() },
                onMenuClick = { navController.navigate(Screen.MainMenu.route) {
                    popUpTo(Screen.MainMenu.route) { inclusive = true }
                } },
                onRestartClick = {
                    val currentLevel = gameViewModel.currentLevel
                    gameViewModel.startLevel(currentLevel)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.LevelComplete.route,
            arguments = listOf(
                navArgument("levelId") { type = NavType.IntType },
                navArgument("score") { type = NavType.IntType },
                navArgument("coins") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val earnedCoins = backStackEntry.arguments?.getInt("coins") ?: 0
            
            LevelCompleteScreen(
                levelId = levelId,
                score = score,
                coinsEarned = earnedCoins,
                onNextLevelClick = {
                    val nextLevel = levelId + 1
                    gameViewModel.startLevel(nextLevel)
                    navController.navigate(Screen.Gameplay.createRoute(nextLevel)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                },
                onReplayClick = {
                    gameViewModel.startLevel(levelId)
                    navController.navigate(Screen.Gameplay.createRoute(levelId)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                }
            )
        }

        composable(Screen.GameOver.route) {
            GameOverScreen(
                onReplayClick = {
                    val currentLevel = gameViewModel.currentLevel
                    gameViewModel.startLevel(currentLevel)
                    navController.navigate(Screen.Gameplay.createRoute(currentLevel)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                },
                onMenuClick = { navController.navigate(Screen.MainMenu.route) {
                    popUpTo(Screen.MainMenu.route) { inclusive = true }
                } }
            )
        }

        composable(Screen.Shop.route) {
            ShopScreen(
                coinBalance = coins?.balance ?: 0,
                onBackClick = { navController.popBackStack() },
                onBuyUpgrade = { item ->
                    gameViewModel.buyUpgrade(item.price, item.id)
                },
                onBuyCoins = { productId ->
                    gameViewModel.purchaseCoinPack(activity, productId)
                },
                onRestorePurchases = {
                    gameViewModel.billingManager.restorePurchases()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                stats = stats,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
