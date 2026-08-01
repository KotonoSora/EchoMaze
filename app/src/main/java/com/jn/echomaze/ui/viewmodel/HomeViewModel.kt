package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.engine.SoundManager
import com.jn.echomaze.ui.viewmodel.home.HomeEffect
import com.jn.echomaze.ui.viewmodel.home.HomeEvent
import com.jn.echomaze.ui.viewmodel.home.HomeUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gameRepository: GameRepository,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

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

    fun onEvent(event: HomeEvent) {
        // No events needed currently as daily reward and level up are removed
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
