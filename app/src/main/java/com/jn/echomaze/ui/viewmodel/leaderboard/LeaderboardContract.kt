package com.jn.echomaze.ui.viewmodel.leaderboard

import com.jn.echomaze.domain.model.GameHistory

data class LeaderboardUiState(
    val history: List<GameHistory> = emptyList(),
    val coinBalance: Int = 0
)

sealed interface LeaderboardEvent {
    // No events currently needed for simplified view
}
