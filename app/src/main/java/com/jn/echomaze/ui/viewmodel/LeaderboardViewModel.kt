package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LeaderboardViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    val topScores: StateFlow<List<GameHistory>> = gameRepository.getTopScores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myHistory: StateFlow<List<GameHistory>> = gameRepository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val achievements: StateFlow<List<Achievement>> = gameRepository.getAllAchievements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
