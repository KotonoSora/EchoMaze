package com.jn.echomaze.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.domain.model.Level
import com.jn.echomaze.domain.model.Puzzle
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.domain.usecase.GetPuzzleUseCase
import com.jn.echomaze.domain.usecase.HandleMoveUseCase
import com.jn.echomaze.domain.usecase.ProcessLevelCompletionUseCase
import com.jn.echomaze.engine.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameplayViewModel(
    private val gameRepository: GameRepository,
    private val soundManager: SoundManager,
    private val getPuzzleUseCase: GetPuzzleUseCase,
    private val handleMoveUseCase: HandleMoveUseCase,
    private val processLevelCompletionUseCase: ProcessLevelCompletionUseCase
) : ViewModel() {

    var currentLevelId by mutableIntStateOf(1)
        private set

    var gridSize by mutableIntStateOf(3)
        private set

    var puzzle by mutableStateOf<Puzzle?>(null)
        private set

    var timeElapsedSeconds by mutableLongStateOf(0L)
        private set

    var isPaused by mutableStateOf(false)
        private set

    var gameOverTriggered by mutableStateOf(false)
        private set

    var isDailyChallenge by mutableStateOf(false)
        private set

    var showNumbersHint by mutableStateOf(false)
        private set

    var showPreviewHint by mutableStateOf(false)
        private set

    val levels: StateFlow<List<Level>> = gameRepository.getAllLevels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val coinBalance: StateFlow<Int> = gameRepository.getCoinBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private var timerJob: Job? = null

    fun startLevel(levelId: Int, isDaily: Boolean = false) {
        currentLevelId = levelId
        isDailyChallenge = isDaily
        timeElapsedSeconds = 0L
        isPaused = false
        gameOverTriggered = false
        showNumbersHint = false
        showPreviewHint = false

        gridSize = when {
            isDaily -> 4
            levelId <= 5 -> 3
            levelId <= 15 -> 4
            else -> 5
        }

        puzzle = getPuzzleUseCase(
            gridSize = gridSize,
            seed = if (isDaily) (System.currentTimeMillis() / 86400000).toInt() else levelId,
            imageRes = com.jn.echomaze.R.drawable.ic_echomaze_foreground_img
        )
        startTimer()
        soundManager.playClick()
    }

    fun toggleNumbersHint() {
        if (coinBalance.value >= 10 || showNumbersHint) {
            if (!showNumbersHint) {
                viewModelScope.launch {
                    gameRepository.updateCoinBalance(coinBalance.value - 10)
                }
            }
            showNumbersHint = !showNumbersHint
            soundManager.playClick()
        }
    }

    fun togglePreviewHint() {
        showPreviewHint = !showPreviewHint
        soundManager.playClick()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!isPaused && !gameOverTriggered && puzzle?.isSolved == false) {
                    timeElapsedSeconds++
                }
            }
        }
    }

    fun handleTileClick(index: Int) {
        val currentPuzzle = puzzle ?: return
        if (isPaused || gameOverTriggered || currentPuzzle.isSolved) return

        val newPuzzle = handleMoveUseCase(currentPuzzle, index)
        if (newPuzzle != currentPuzzle) {
            puzzle = newPuzzle
            soundManager.playMove()

            if (newPuzzle.isSolved) {
                onLevelComplete()
            } else if (newPuzzle.moves >= getMaxMoves(newPuzzle.gridSize)) {
                triggerGameOver()
            }
        }
    }

    private fun triggerGameOver() {
        gameOverTriggered = true
        soundManager.playLose()
    }

    private fun getMaxMoves(gridSize: Int): Int = gridSize * gridSize * 15

    private fun onLevelComplete() {
        soundManager.playWin()
        viewModelScope.launch {
            val stars = calculateStars()
            processLevelCompletionUseCase(
                levelId = currentLevelId,
                score = calculateScore(),
                moves = puzzle?.moves ?: 0,
                stars = stars,
                isDailyChallenge = isDailyChallenge
            )
        }
    }

    private fun calculateStars(): Int {
        val p = puzzle ?: return 0
        val baseMoves = p.gridSize * p.gridSize * 10
        return when {
            p.moves <= baseMoves -> 3
            p.moves <= baseMoves * 1.5 -> 2
            else -> 1
        }
    }

    private fun calculateScore(): Int = (puzzle?.moves ?: 0) * 5

    fun togglePause() {
        isPaused = !isPaused
        soundManager.playClick()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
