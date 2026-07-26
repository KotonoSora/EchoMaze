package com.jn.echomaze.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.engine.SoundManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gameRepository: GameRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    val stats: StateFlow<UserStats?> = gameRepository.getGameStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val coinBalance: StateFlow<Int> = gameRepository.getCoinBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    var canClaimDailyReward by mutableStateOf(false)
        private set

    init {
        checkDailyAvailability()
        checkLoginStreak()
    }

    private fun checkDailyAvailability() {
        viewModelScope.launch {
            stats.collect { statsValue ->
                statsValue?.let {
                    val now = System.currentTimeMillis()
                    canClaimDailyReward = now - it.lastDailyRewardClaimed >= 86400000
                }
            }
        }
    }

    private fun checkLoginStreak() {
        viewModelScope.launch {
            val currentStats = gameRepository.getGameStats().first() ?: UserStats()
            val now = System.currentTimeMillis()
            val lastLogin = currentStats.lastLoginTimestamp

            val diff = now - lastLogin
            val oneDay = 86400000L

            if (diff >= oneDay && diff < oneDay * 2) {
                // Consecutive day
                val newStreak = currentStats.loginStreak + 1
                gameRepository.updateStats(
                    currentStats.copy(
                        loginStreak = newStreak,
                        lastLoginTimestamp = now
                    )
                )
                // Award coins
                val currentCoins = gameRepository.getCoinBalance().first()
                gameRepository.updateCoinBalance(currentCoins + (20 * newStreak))
                soundManager.playWin()
            } else if (diff >= oneDay * 2) {
                // Streak broken
                gameRepository.updateStats(
                    currentStats.copy(
                        loginStreak = 1,
                        lastLoginTimestamp = now
                    )
                )
            } else if (lastLogin == 0L) {
                // First login
                gameRepository.updateStats(currentStats.copy(lastLoginTimestamp = now))
            }
        }
    }

    fun claimDailyReward() {
        viewModelScope.launch {
            if (canClaimDailyReward) {
                soundManager.playWin()
                val currentCoins = gameRepository.getCoinBalance().first()
                gameRepository.updateCoinBalance(currentCoins + 50)

                val currentStats = gameRepository.getGameStats().first() ?: UserStats()
                gameRepository.updateStats(currentStats.copy(lastDailyRewardClaimed = System.currentTimeMillis()))
                canClaimDailyReward = false
            }
        }
    }
}
