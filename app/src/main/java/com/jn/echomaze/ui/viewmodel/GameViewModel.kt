package com.jn.echomaze.ui.viewmodel

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.billing.BillingManager
import com.jn.echomaze.data.AppDatabase
import com.jn.echomaze.data.CoinEntity
import com.jn.echomaze.data.LevelEntity
import com.jn.echomaze.data.StatsEntity
import com.jn.echomaze.engine.MazeData
import com.jn.echomaze.engine.MazeGenerator
import com.jn.echomaze.engine.PulseEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val dao = database.gameDao()
    private val mazeGenerator = MazeGenerator()
    
    val billingManager = BillingManager(application, viewModelScope)
    val pulseEngine = PulseEngine(viewModelScope)
    
    var currentLevel by mutableIntStateOf(1)
        private set
    
    var coinsEarned by mutableIntStateOf(0)
        private set
        
    var score by mutableIntStateOf(0)
        private set
        
    var timeElapsedSeconds by mutableLongStateOf(0L)
        private set
        
    var isPaused by mutableStateOf(false)
        private set
        
    var pulsesRemaining by mutableIntStateOf(10)
        private set

    var mazeData by mutableStateOf<MazeData?>(null)
        private set

    var playerPos by mutableStateOf(Offset(100f, 100f))

    // Upgradable Stats
    var pulseRadius by mutableFloatStateOf(400f)
        private set
    var pulseDuration by mutableLongStateOf(2500L)
        private set
    var isHintActive by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    init {
        billingManager.startConnection()
    }

    private fun updateGameStats(update: (StatsEntity) -> StatsEntity) {
        viewModelScope.launch {
            val currentStats = dao.getGameStats().first() ?: StatsEntity()
            dao.updateStats(update(currentStats))
        }
    }

    fun startLevel(levelId: Int) {
        currentLevel = levelId
        coinsEarned = 0
        score = 0
        timeElapsedSeconds = 0
        isPaused = false
        isHintActive = false
        pulsesRemaining = 8 + levelId
        
        val data = mazeGenerator.generateMaze(1080f, 1920f, levelId)
        mazeData = data
        playerPos = data.startPos
        
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!isPaused) {
                    timeElapsedSeconds++
                }
            }
        }
    }

    fun handleMove(newPos: Offset) {
        if (isPaused) return
        
        val data = mazeData ?: return
        
        // Simple collision detection
        val playerRadius = 20f
        val collision = data.walls.any { wall ->
            val closestX = newPos.x.coerceIn(wall.bounds.left, wall.bounds.right)
            val closestY = newPos.y.coerceIn(wall.bounds.top, wall.bounds.bottom)
            val dx = newPos.x - closestX
            val dy = newPos.y - closestY
            sqrt(dx * dx + dy * dy) < playerRadius
        }

        if (!collision) {
            playerPos = newPos
            
            // Check win condition
            val distToExit = (playerPos - data.exitPos).getDistance()
            if (distToExit < 50f) {
                completeLevel(stars = 3)
            }
        }
    }

    fun triggerPulse(x: Float, y: Float) {
        if (!isPaused && pulsesRemaining > 0) {
            pulsesRemaining--
            pulseEngine.triggerPulse(x, y, radius = pulseRadius, duration = pulseDuration)
            score += 10
            updateGameStats { it.copy(totalPulsesUsed = it.totalPulsesUsed + 1) }
        }
    }

    fun buyExtraPulses() {
        viewModelScope.launch {
            val balance = dao.getCoinBalance().first()?.balance ?: 0
            val cost = 50
            if (balance >= cost) {
                dao.updateCoinBalance(CoinEntity(balance = balance - cost))
                pulsesRemaining += 3
            }
        }
    }

    fun buyRevealMap() {
        viewModelScope.launch {
            val balance = dao.getCoinBalance().first()?.balance ?: 0
            val cost = 100
            if (balance >= cost) {
                dao.updateCoinBalance(CoinEntity(balance = balance - cost))
                pulseEngine.triggerPulse(540f, 960f, radius = 3000f, duration = 4000L)
            }
        }
    }

    fun buyHintPath() {
        viewModelScope.launch {
            val balance = dao.getCoinBalance().first()?.balance ?: 0
            val cost = 75
            if (balance >= cost) {
                dao.updateCoinBalance(CoinEntity(balance = balance - cost))
                isHintActive = true
                delay(5000)
                isHintActive = false
            }
        }
    }

    fun completeLevel(stars: Int) {
        timerJob?.cancel()
        viewModelScope.launch {
            val level = dao.getLevelById(currentLevel) ?: return@launch
            dao.updateLevel(level.copy(isCompleted = true, starsEarned = stars))
            
            val nextLevelId = currentLevel + 1
            val nextLevel = dao.getLevelById(nextLevelId)
            if (nextLevel != null) {
                dao.updateLevel(nextLevel.copy(isUnlocked = true))
            } else {
                dao.insertLevels(listOf(LevelEntity(id = nextLevelId, levelNumber = nextLevelId, isUnlocked = true)))
            }
            
            addCoins(coinsEarned + 100) // Bonus for completion
        }
    }

    private suspend fun addCoins(amount: Int) {
        val currentBalance = dao.getCoinBalance().first()?.balance ?: 0
        dao.updateCoinBalance(CoinEntity(balance = currentBalance + amount))
    }

    fun buyUpgrade(price: Int, upgradeId: String) {
        viewModelScope.launch {
            val balance = dao.getCoinBalance().first()?.balance ?: 0
            if (balance >= price) {
                dao.updateCoinBalance(CoinEntity(balance = balance - price))
                applyUpgrade(upgradeId)
            }
        }
    }

    private fun applyUpgrade(upgradeId: String) {
        when (upgradeId) {
            "radius" -> pulseRadius += 50f
            "duration" -> pulseDuration += 500L
        }
    }

    fun purchaseCoinPack(activity: Activity, productId: String) {
        billingManager.launchBillingFlow(activity, productId)
    }
}
