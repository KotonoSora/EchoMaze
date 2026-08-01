package com.jn.echomaze.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jn.echomaze.MainApplication
import com.jn.echomaze.ui.viewmodel.GameplayViewModel
import com.jn.echomaze.ui.viewmodel.HomeViewModel
import com.jn.echomaze.ui.viewmodel.LeaderboardViewModel
import com.jn.echomaze.ui.viewmodel.SettingsViewModel
import com.jn.echomaze.ui.viewmodel.ShopViewModel

/**
 * Provides [ViewModelProvider.Factory] to create ViewModels for the entire app.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                gameRepository = application().container.gameRepository,
                soundManager = application().container.soundManager
            )
        }

        // Initializer for GameplayViewModel
        initializer {
            GameplayViewModel(
                gameRepository = application().container.gameRepository,
                soundManager = application().container.soundManager,
                getPuzzleUseCase = application().container.getPuzzleUseCase,
                handleMoveUseCase = application().container.handleMoveUseCase,
                processGameCompletionUseCase = application().container.processGameCompletionUseCase
            )
        }

        // Initializer for ShopViewModel
        initializer {
            ShopViewModel(
                gameRepository = application().container.gameRepository,
                billingManager = application().container.billingManager,
                soundManager = application().container.soundManager
            )
        }

        // Initializer for LeaderboardViewModel
        initializer {
            LeaderboardViewModel(
                gameRepository = application().container.gameRepository
            )
        }

        // Initializer for SettingsViewModel
        initializer {
            SettingsViewModel(
                gameRepository = application().container.gameRepository
            )
        }
    }
}

/**
 * Extension function to queries for [MainApplication] object and returns an instance of
 * [MainApplication].
 */
fun CreationExtras.application(): MainApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
