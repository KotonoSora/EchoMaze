package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.domain.usecase.GetPuzzleInput
import com.jn.echomaze.domain.usecase.GetPuzzleUseCase
import com.jn.echomaze.domain.usecase.HandleMoveInput
import com.jn.echomaze.domain.usecase.HandleMoveUseCase
import com.jn.echomaze.domain.usecase.ProcessGameCompletionInput
import com.jn.echomaze.domain.usecase.ProcessGameCompletionUseCase
import com.jn.echomaze.engine.SoundManager
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayEffect
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayEvent
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameplayViewModel(
    private val gameRepository: GameRepository,
    private val soundManager: SoundManager,
    private val getPuzzleUseCase: GetPuzzleUseCase,
    private val handleMoveUseCase: HandleMoveUseCase,
    private val processGameCompletionUseCase: ProcessGameCompletionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameplayUiState())
    val uiState: StateFlow<GameplayUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<GameplayEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private var timerJob: Job? = null

    private val puzzleImages = listOf(
        com.jn.echomaze.R.drawable.assets_1,
        com.jn.echomaze.R.drawable.assets_2,
        com.jn.echomaze.R.drawable.assets_3,
        com.jn.echomaze.R.drawable.assets_4,
        com.jn.echomaze.R.drawable.assets_5
    )

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            gameRepository.getUserStats().collectLatest { stats ->
                _uiState.update { it.copy(stats = stats, coinBalance = stats.coinBalance) }
            }
        }
    }

    fun onEvent(event: GameplayEvent) {
        when (event) {
            is GameplayEvent.StartPuzzle -> startNewPuzzle(event.seed)
            GameplayEvent.ToggleNumbersHint -> toggleNumbersHint()
            GameplayEvent.TogglePreviewHint -> togglePreviewHint()
            is GameplayEvent.OnTileClick -> handleTileClick(event.index)
            GameplayEvent.TogglePause -> togglePause()
            GameplayEvent.DismissVictory -> _uiState.update { it.copy(isSolved = false) }
        }
    }

    private fun startNewPuzzle(seed: Int) {
        _uiState.update {
            it.copy(
                timeElapsedSeconds = 0L,
                isPaused = false,
                isSolved = false,
                showNumbersHint = false,
                showPreviewHint = false,
                gridSize = 3
            )
        }

        val random = Random(seed)
        val randomImage = puzzleImages[random.nextInt(puzzleImages.size)]

        val newPuzzle = getPuzzleUseCase(
            GetPuzzleInput(
                gridSize = 3,
                seed = seed,
                imageRes = randomImage
            )
        )
        _uiState.update { it.copy(puzzle = newPuzzle) }

        startTimer()
        emitEffect(GameplayEffect.PlayClickSound)
    }

    private fun toggleNumbersHint() {
        val state = _uiState.value
        if (state.coinBalance >= 10 || state.showNumbersHint) {
            if (!state.showNumbersHint) {
                viewModelScope.launch {
                    val currentStats = gameRepository.getUserStats().first()
                    gameRepository.updateStats(currentStats.copy(
                        coinBalance = currentStats.coinBalance - 10
                    ))
                }
            }
            _uiState.update { it.copy(showNumbersHint = !it.showNumbersHint) }
            emitEffect(GameplayEffect.PlayClickSound)
        }
    }

    private fun togglePreviewHint() {
        _uiState.update { it.copy(showPreviewHint = !it.showPreviewHint) }
        emitEffect(GameplayEffect.PlayClickSound)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val state = _uiState.value
                if (!state.isPaused && !state.isSolved && state.puzzle?.isSolved == false) {
                    _uiState.update { it.copy(timeElapsedSeconds = it.timeElapsedSeconds + 1) }
                }
            }
        }
    }

    private fun handleTileClick(index: Int) {
        val state = _uiState.value
        val currentPuzzle = state.puzzle ?: return
        if (state.isPaused || state.isSolved || currentPuzzle.isSolved) return

        val newPuzzle = handleMoveUseCase(HandleMoveInput(currentPuzzle, index))
        if (newPuzzle != currentPuzzle) {
            _uiState.update { it.copy(puzzle = newPuzzle) }
            emitEffect(GameplayEffect.PlayMoveSound)

            if (newPuzzle.isSolved) {
                onVictory()
            }
        }
    }

    private fun onVictory() {
        emitEffect(GameplayEffect.PlayWinSound)
        
        val state = _uiState.value
        val isDaily = state.puzzle?.seed == 0
        val finalScore = calculateScore(state.puzzle, state.timeElapsedSeconds)
        val finalCoins = if (isDaily) 200 else 50

        _uiState.update { 
            it.copy(
                isSolved = true,
                earnedScore = finalScore,
                earnedCoins = finalCoins,
                isDailyChallenge = isDaily
            )
        }

        viewModelScope.launch {
            processGameCompletionUseCase(
                ProcessGameCompletionInput(
                    score = finalScore,
                    moves = state.puzzle?.moves ?: 0,
                    isDailyChallenge = isDaily
                )
            )
        }
    }

    private fun calculateScore(puzzle: com.jn.echomaze.domain.model.Puzzle?, timeElapsed: Long): Int {
        val p = puzzle ?: return 0
        val baseMoves = p.gridSize * p.gridSize * 10
        val moveBonus = (baseMoves * 2 - p.moves).coerceAtLeast(0) * 10
        val timeBonus = (300 - timeElapsed.toInt()).coerceAtLeast(0) * 2
        return moveBonus + timeBonus
    }

    private fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
        emitEffect(GameplayEffect.PlayClickSound)
    }

    private fun emitEffect(effect: GameplayEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
