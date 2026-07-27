package com.jn.echomaze.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jn.echomaze.ui.AppViewModelProvider
import com.jn.echomaze.ui.screens.DailyChallengeScreen
import com.jn.echomaze.ui.screens.GameOverScreen
import com.jn.echomaze.ui.screens.GameplayScreen
import com.jn.echomaze.ui.screens.HelpScreen
import com.jn.echomaze.ui.screens.HomeScreen
import com.jn.echomaze.ui.screens.LeaderboardScreen
import com.jn.echomaze.ui.screens.LevelCompleteScreen
import com.jn.echomaze.ui.screens.LevelSelectScreen
import com.jn.echomaze.ui.screens.PauseScreen
import com.jn.echomaze.ui.screens.SettingsScreen
import com.jn.echomaze.ui.screens.ShopScreen
import com.jn.echomaze.ui.screens.SkinsShopScreen
import com.jn.echomaze.ui.theme.GameTheme
import com.jn.echomaze.ui.viewmodel.GameplayViewModel
import com.jn.echomaze.ui.viewmodel.HomeViewModel
import com.jn.echomaze.ui.viewmodel.LeaderboardViewModel
import com.jn.echomaze.ui.viewmodel.SettingsViewModel
import com.jn.echomaze.ui.viewmodel.ShopViewModel

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as Activity

    // GameplayViewModel is shared across gameplay-related screens
    val gameplayViewModel: GameplayViewModel = viewModel(factory = AppViewModelProvider.Factory)

    // Observe Game Over
    LaunchedEffect(gameplayViewModel.gameOverTriggered) {
        if (gameplayViewModel.gameOverTriggered) {
            navController.navigate(Screen.GameOver.route)
        }
    }

    // Observe Level Complete
    LaunchedEffect(gameplayViewModel.levelCompleteTriggered) {
        if (gameplayViewModel.levelCompleteTriggered) {
            val score = gameplayViewModel.calculateScore()
            val stars = gameplayViewModel.calculateStars()
            val coins = if (gameplayViewModel.isDailyChallenge) 200 else (stars * 50)
            navController.navigate(
                Screen.LevelComplete.createRoute(
                    gameplayViewModel.currentLevelId,
                    score,
                    coins,
                    stars,
                    gameplayViewModel.timeElapsedSeconds.toInt()
                )
            )
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val stats by viewModel.stats.collectAsState()
            val coinBalance by viewModel.coinBalance.collectAsState()

            HomeScreen(
                coinBalance = coinBalance,
                stats = stats,
                canClaimDailyReward = viewModel.canClaimDailyReward,
                onClaimDailyReward = { viewModel.claimDailyReward() },
                onPlayClick = { navController.navigate(Screen.LevelSelect.route) },
                onDailyChallengeClick = { navController.navigate(Screen.DailyChallenge.route) },
                onLeaderboardClick = { navController.navigate(Screen.Leaderboard.route) },
                onSkinsShopClick = { navController.navigate(Screen.SkinsShop.route) },
                onHelpClick = { navController.navigate(Screen.Help.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onShopClick = { navController.navigate(Screen.Shop.route) },
                levelUpToCelebrate = viewModel.levelUpCelebration,
                onDismissLevelUp = { viewModel.dismissLevelUp() }
            )
        }

        composable(route = Screen.LevelSelect.route) {
            val levels by gameplayViewModel.levels.collectAsState()
            val coinBalance by gameplayViewModel.coinBalance.collectAsState()
            LevelSelectScreen(
                levels = levels,
                coinBalance = coinBalance,
                onLevelClick = { levelId ->
                    gameplayViewModel.startLevel(levelId)
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
            val coinBalance by gameplayViewModel.coinBalance.collectAsState()
            val stats by gameplayViewModel.stats.collectAsState()
            val puzzle = gameplayViewModel.puzzle
            val theme = GameTheme.getThemeById(stats?.selectedThemeId ?: "skin_neon")

            GameplayScreen(
                levelId = levelId,
                coins = coinBalance,
                movesCount = puzzle?.moves ?: 0,
                maxMoves = gameplayViewModel.gridSize * gameplayViewModel.gridSize * 15,
                timeSeconds = gameplayViewModel.timeElapsedSeconds,
                gridSize = puzzle?.gridSize ?: 3,
                tiles = puzzle?.tiles ?: emptyList(),
                imageRes = puzzle?.imageRes,
                showNumbersHint = gameplayViewModel.showNumbersHint,
                showPreviewHint = gameplayViewModel.showPreviewHint,
                theme = theme,
                onTileClick = { gameplayViewModel.handleTileClick(it) },
                onPauseClick = { navController.navigate(Screen.Pause.route) },
                onQuickBuyCoins = { navController.navigate(Screen.Shop.route) },
                onHintNumbersClick = { gameplayViewModel.toggleNumbersHint() },
                onHintPreviewClick = { gameplayViewModel.togglePreviewHint() }
            )
        }

        composable(route = Screen.DailyChallenge.route) {
            val coinBalance by gameplayViewModel.coinBalance.collectAsState()
            DailyChallengeScreen(
                coinBalance = coinBalance,
                onBackClick = { navController.popBackStack() },
                onPlayClick = {
                    gameplayViewModel.startLevel(0, isDaily = true)
                    navController.navigate(Screen.Gameplay.createRoute(0))
                },
                isAlreadyCompleted = false
            )
        }

        composable(route = Screen.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val topScores by viewModel.topScores.collectAsState()
            val myHistory by viewModel.myHistory.collectAsState()
            val achievements by viewModel.achievements.collectAsState()
            val coinBalance by viewModel.coinBalance.collectAsState()

            LeaderboardScreen(
                coinBalance = coinBalance,
                onBackClick = { navController.popBackStack() },
                topScores = topScores,
                myHistory = myHistory,
                achievements = achievements
            )
        }

        composable(route = Screen.Pause.route) {
            PauseScreen(
                onResumeClick = { navController.popBackStack() },
                onMenuClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onRestartClick = {
                    gameplayViewModel.startLevel(
                        gameplayViewModel.currentLevelId,
                        gameplayViewModel.isDailyChallenge
                    )
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.LevelComplete.route,
            arguments = listOf(
                navArgument("levelId") { type = NavType.IntType },
                navArgument("score") { type = NavType.IntType },
                navArgument("coins") { type = NavType.IntType },
                navArgument("stars") { type = NavType.IntType },
                navArgument("time") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val earnedCoins = backStackEntry.arguments?.getInt("coins") ?: 0
            val stars = backStackEntry.arguments?.getInt("stars") ?: 0
            val time = backStackEntry.arguments?.getInt("time") ?: 0
            val stats by gameplayViewModel.stats.collectAsState()

            LevelCompleteScreen(
                levelId = levelId,
                score = score,
                coinsEarned = earnedCoins,
                stars = stars,
                timeSeconds = time,
                stats = stats,
                onNextLevelClick = {
                    val nextLevel = levelId + 1
                    gameplayViewModel.startLevel(nextLevel)
                    navController.navigate(Screen.Gameplay.createRoute(nextLevel)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                },
                onReplayClick = {
                    gameplayViewModel.startLevel(levelId)
                    navController.navigate(Screen.Gameplay.createRoute(levelId)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                }
            )
        }

        composable(route = Screen.GameOver.route) {
            GameOverScreen(
                onReplayClick = {
                    gameplayViewModel.startLevel(
                        gameplayViewModel.currentLevelId,
                        gameplayViewModel.isDailyChallenge
                    )
                    navController.navigate(Screen.Gameplay.createRoute(gameplayViewModel.currentLevelId)) {
                        popUpTo(Screen.LevelSelect.route)
                    }
                },
                onMenuClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Shop.route) {
            val viewModel: ShopViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val coinBalance by viewModel.coinBalance.collectAsState()
            val coinProducts by viewModel.products.collectAsState()
            val isStoreAvailable by viewModel.isStoreAvailable.collectAsState()

            ShopScreen(
                coinBalance = coinBalance,
                isStoreAvailable = isStoreAvailable,
                coinProducts = coinProducts,
                adCooldown = viewModel.adCooldownSeconds,
                onBackClick = { navController.popBackStack() },
                onWatchAd = { viewModel.watchAdForCoins() },
                onBuyProduct = { product ->
                    viewModel.purchaseCoinPack(activity, product)
                },
                onBuyUpgrade = { id ->
                    viewModel.buyUpgrade(id)
                },
                onRestorePurchases = {
                    viewModel.billingManager.restorePurchases()
                }
            )
        }

        composable(route = Screen.SkinsShop.route) {
            val viewModel: ShopViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val coinBalance by viewModel.coinBalance.collectAsState()
            val stats by gameplayViewModel.stats.collectAsState()

            SkinsShopScreen(
                coinBalance = coinBalance,
                stats = stats,
                onBackClick = { navController.popBackStack() },
                onBuyUpgrade = { id ->
                    viewModel.buyUpgrade(id)
                }
            )
        }

        composable(route = Screen.Help.route) {
            val coinBalance by gameplayViewModel.coinBalance.collectAsState()
            HelpScreen(
                coinBalance = coinBalance,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val stats by viewModel.stats.collectAsState()
            val coinBalance by viewModel.coinBalance.collectAsState()
            SettingsScreen(
                stats = stats,
                coinBalance = coinBalance,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
