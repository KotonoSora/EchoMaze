package com.jn.echomaze.ui.viewmodel.gameplay

import com.jn.echomaze.domain.model.Puzzle
import com.jn.echomaze.domain.model.UserStats

data class GameplayUiState(
    val gridSize: Int = 3,
    val puzzle: Puzzle? = null,
    val timeElapsedSeconds: Long = 0L,
    val isPaused: Boolean = false,
    val isSolved: Boolean = false,
    val isDailyChallenge: Boolean = false,
    val showNumbersHint: Boolean = false,
    val showPreviewHint: Boolean = false,
    val coinBalance: Int = 0,
    val stats: UserStats? = null,
    val earnedScore: Int = 0,
    val earnedCoins: Int = 0
)

sealed interface GameplayEvent {
    data class StartPuzzle(val seed: Int) : GameplayEvent
    data object ToggleNumbersHint : GameplayEvent
    data object TogglePreviewHint : GameplayEvent
    data class OnTileClick(val index: Int) : GameplayEvent
    data object TogglePause : GameplayEvent
    data object DismissVictory : GameplayEvent
}

sealed interface GameplayEffect {
    data object PlayMoveSound : GameplayEffect
    data object PlayClickSound : GameplayEffect
    data object PlayWinSound : GameplayEffect
    data object PlayLoseSound : GameplayEffect
    data object NavigateBack : GameplayEffect
}
