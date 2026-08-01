package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.ui.viewmodel.leaderboard.LeaderboardEvent
import com.jn.echomaze.ui.viewmodel.leaderboard.LeaderboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                gameRepository.getHistory(),
                gameRepository.getUserStats()
            ) { history, stats ->
                _uiState.update {
                    it.copy(
                        history = history,
                        coinBalance = stats.coinBalance
                    )
                }
            }.collectLatest { }
        }
    }

    fun onEvent(event: LeaderboardEvent) {
        // No events to handle
    }
}
