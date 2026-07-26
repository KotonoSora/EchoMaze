package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.first

class ProcessLevelCompletionUseCase(
    private val repository: GameRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) {
    suspend operator fun invoke(
        levelId: Int,
        score: Int,
        moves: Int,
        stars: Int,
        isDailyChallenge: Boolean
    ) {
        val rewardCoins = if (isDailyChallenge) 200 else (stars * 50)
        val xpEarned = if (isDailyChallenge) 500 else (100 + (stars * 20))

        if (!isDailyChallenge) {
            val level = repository.getLevelById(levelId)
            if (level != null) {
                repository.updateLevel(level.copy(isCompleted = true, starsEarned = stars))

                // Unlock next level
                val nextLevelId = levelId + 1
                val nextLevel = repository.getLevelById(nextLevelId)
                if (nextLevel != null) {
                    repository.updateLevel(nextLevel.copy(isUnlocked = true))
                }
            }
        } else {
            val stats = repository.getGameStats().first() ?: UserStats()
            repository.updateStats(stats.copy(lastDailyChallengeCompleted = System.currentTimeMillis()))
        }

        // Add history
        repository.insertHistory(
            GameHistory(
                score = score,
                moves = moves,
                rewardCoins = rewardCoins,
                levelId = levelId,
                isDailyChallenge = isDailyChallenge,
                timestamp = System.currentTimeMillis()
            )
        )

        // Update stats
        val currentStats = repository.getGameStats().first() ?: UserStats()
        repository.updateStats(
            currentStats.copy(
                totalLevelsCompleted = currentStats.totalLevelsCompleted + 1,
                totalXp = currentStats.totalXp + xpEarned,
                totalCoinsCollected = currentStats.totalCoinsCollected + rewardCoins
            )
        )

        // Add coins
        val currentCoins = repository.getCoinBalance().first()
        repository.updateCoinBalance(currentCoins + rewardCoins)

        // Check achievements
        checkAchievementsUseCase()
    }
}
