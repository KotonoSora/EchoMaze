package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.ui.viewmodel.settings.SettingsEvent
import com.jn.echomaze.ui.viewmodel.settings.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            gameRepository.getUserStats().collectLatest { stats ->
                _uiState.update {
                    it.copy(
                        stats = stats,
                        coinBalance = stats.coinBalance
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        viewModelScope.launch {
            val currentStats = gameRepository.getUserStats().first()
            when (event) {
                is SettingsEvent.OnSoundToggled -> {
                    gameRepository.updateStats(currentStats.copy(isSoundEnabled = event.enabled))
                }
                is SettingsEvent.OnMusicToggled -> {
                    gameRepository.updateStats(currentStats.copy(isMusicEnabled = event.enabled))
                }
                SettingsEvent.OnResetStats -> {
                    gameRepository.resetGame()
                }
            }
        }
    }
}
