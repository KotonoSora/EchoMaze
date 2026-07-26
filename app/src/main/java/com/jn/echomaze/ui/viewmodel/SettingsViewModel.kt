package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {
    val stats: StateFlow<UserStats?> = gameRepository.getGameStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
