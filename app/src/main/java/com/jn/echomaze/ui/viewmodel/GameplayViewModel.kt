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
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.domain.usecase.GetPuzzleUseCase
import com.jn.echomaze.domain.usecase.HandleMoveUseCase
import com.jn.echomaze.domain.usecase.ProcessLevelCompletionUseCase
import com.jn.echomaze.engine.SoundManager
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    var levelCompleteTriggered by mutableStateOf(false)
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

    val stats: StateFlow<UserStats?> = gameRepository.getGameStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val puzzleImages = listOf(
        com.jn.echomaze.R.drawable.assets_1,
        com.jn.echomaze.R.drawable.assets_2,
        com.jn.echomaze.R.drawable.assets_3,
        com.jn.echomaze.R.drawable.assets_4,
        com.jn.echomaze.R.drawable.assets_5
    )

    private var timerJob: Job? = null

    fun startLevel(levelId: Int, isDaily: Boolean = false) {
        currentLevelId = levelId
        isDailyChallenge = isDaily
        timeElapsedSeconds = 0L
        isPaused = false
        gameOverTriggered = false
        levelCompleteTriggered = false
        showNumbersHint = false
        showPreviewHint = false

        gridSize = 3 // Enforce 3x3 as requested for "9 parts"

        val seed = if (isDaily) (System.currentTimeMillis() / 86400000).toInt() else levelId
        val random = Random(seed)
        val randomImage = puzzleImages[random.nextInt(puzzleImages.size)]

        puzzle = getPuzzleUseCase(
            gridSize = gridSize,
            seed = seed,
            imageRes = randomImage
        )
        startTimer()
        soundManager.playClick()
    }

    fun toggleNumbersHint() {
        if (coinBalance.value >= 10 || showNumbersHint) {
            if (!showNumbersHint) {
                viewModelScope.launch {
                    gameRepository.updateCoinBalance(coinBalance.value - 10)
                    val currentStats = gameRepository.getGameStats().first() ?: UserStats()
                    gameRepository.updateStats(currentStats.copy(hintsUsed = currentStats.hintsUsed + 1))
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
        levelCompleteTriggered = true
        viewModelScope.launch {
            val stars = calculateStars()
            processLevelCompletionUseCase(
                levelId = currentLevelId,
                score = calculateScore(),
                moves = puzzle?.moves ?: 0,
                stars = stars,
                timeSeconds = timeElapsedSeconds.toInt(),
                isDailyChallenge = isDailyChallenge
            )
        }
    }

    fun calculateStars(): Int {
        val p = puzzle ?: return 0
        val baseMoves = p.gridSize * p.gridSize * 10
        return when {
            p.moves <= baseMoves -> 3
            p.moves <= baseMoves * 1.5 -> 2
            else -> 1
        }
    }

    fun calculateScore(): Int {
        val p = puzzle ?: return 0
        val baseMoves = p.gridSize * p.gridSize * 10
        val moveBonus = (baseMoves * 2 - p.moves).coerceAtLeast(0) * 10
        val timeBonus = (300 - timeElapsedSeconds.toInt()).coerceAtLeast(0) * 2
        return moveBonus + timeBonus
    }

    fun togglePause() {
        isPaused = !isPaused
        soundManager.playClick()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
