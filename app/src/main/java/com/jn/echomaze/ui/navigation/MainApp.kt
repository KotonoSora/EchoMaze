package com.jn.echomaze.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jn.echomaze.MainApplication
import com.jn.echomaze.ui.AppViewModelProvider
import com.jn.echomaze.ui.screens.DailyChallengeScreen
import com.jn.echomaze.ui.screens.GameplayScreen
import com.jn.echomaze.ui.screens.HelpScreen
import com.jn.echomaze.ui.screens.HomeScreen
import com.jn.echomaze.ui.screens.LeaderboardScreen
import com.jn.echomaze.ui.screens.PauseScreen
import com.jn.echomaze.ui.screens.SettingsScreen
import com.jn.echomaze.ui.screens.ShopScreen
import com.jn.echomaze.ui.theme.GameTheme
import com.jn.echomaze.ui.viewmodel.GameplayViewModel
import com.jn.echomaze.ui.viewmodel.HomeViewModel
import com.jn.echomaze.ui.viewmodel.LeaderboardViewModel
import com.jn.echomaze.ui.viewmodel.SettingsViewModel
import com.jn.echomaze.ui.viewmodel.ShopViewModel
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayEffect
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayEvent
import com.jn.echomaze.ui.viewmodel.home.HomeEffect
import com.jn.echomaze.ui.viewmodel.leaderboard.LeaderboardEvent
import com.jn.echomaze.ui.viewmodel.shop.ShopEffect
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as MainApplication
    val soundManager = app.container.soundManager

    // Shared ViewModels
    val gameplayViewModel: GameplayViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val gameplayState by gameplayViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        gameplayViewModel.uiEffect.collectLatest { effect ->
            val soundEnabled = gameplayViewModel.uiState.value.stats?.isSoundEnabled ?: true
            when (effect) {
                GameplayEffect.PlayMoveSound -> if (soundEnabled) soundManager.playMove()
                GameplayEffect.PlayClickSound -> if (soundEnabled) soundManager.playClick()
                GameplayEffect.PlayWinSound -> if (soundEnabled) soundManager.playWin()
                GameplayEffect.PlayLoseSound -> if (soundEnabled) soundManager.playLose()
                GameplayEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val state by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        HomeEffect.PlayWinSound -> soundManager.playWin()
                    }
                }
            }

            HomeScreen(
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onPlayClick = {
                    gameplayViewModel.onEvent(GameplayEvent.StartPuzzle(Random.nextInt()))
                    navController.navigate(Screen.Gameplay.route)
                },
                onDailyChallengeClick = { navController.navigate(Screen.DailyChallenge.route) },
                onLeaderboardClick = { navController.navigate(Screen.Leaderboard.route) },
                onHelpClick = { navController.navigate(Screen.Help.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onShopClick = { navController.navigate(Screen.Shop.route) }
            )
        }

        composable(route = Screen.DailyChallenge.route) {
            DailyChallengeScreen(
                state = gameplayState,
                onEvent = { gameplayViewModel.onEvent(it) },
                onBackClick = { navController.popBackStack() },
                onPlayClick = {
                    navController.navigate(Screen.Gameplay.route)
                }
            )
        }

        composable(route = Screen.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val state by viewModel.uiState.collectAsState()

            LeaderboardScreen(
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Gameplay.route) {
            GameplayScreen(
                state = gameplayState,
                onEvent = { gameplayViewModel.onEvent(it) },
                onPauseClick = { navController.navigate(Screen.Pause.route) },
                onQuickBuyCoins = { navController.navigate(Screen.Shop.route) },
                theme = GameTheme.NeonBlueTheme
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
                    gameplayViewModel.onEvent(GameplayEvent.StartPuzzle(Random.nextInt()))
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.Shop.route) {
            val viewModel: ShopViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val state by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.uiEffect.collectLatest { effect ->
                    when (effect) {
                        ShopEffect.PlayClickSound -> soundManager.playClick()
                        ShopEffect.PlayWinSound -> soundManager.playWin()
                    }
                }
            }

            ShopScreen(
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Help.route) {
            HelpScreen(
                coinBalance = gameplayState.coinBalance,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val state by viewModel.uiState.collectAsState()
            SettingsScreen(
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
