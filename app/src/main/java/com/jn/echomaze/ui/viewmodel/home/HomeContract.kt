package com.jn.echomaze.ui.viewmodel.home

import com.jn.echomaze.domain.model.UserStats

data class HomeUiState(
    val stats: UserStats? = null,
    val coinBalance: Int = 0,
    val canClaimDailyReward: Boolean = false,
    val levelUpCelebration: Int? = null
)

sealed interface HomeEvent {
    data object ClaimDailyReward : HomeEvent
    data object DismissLevelUp : HomeEvent
}

sealed interface HomeEffect {
    data object PlayWinSound : HomeEffect
}
