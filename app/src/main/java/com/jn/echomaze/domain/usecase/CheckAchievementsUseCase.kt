package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.first

class CheckAchievementsUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(): List<Achievement> {
        val stats = repository.getGameStats().first() ?: UserStats()
        val allAchievements = repository.getAllAchievements().first()
        val newlyUnlocked = mutableListOf<Achievement>()

        allAchievements.forEach { achievement ->
            if (!achievement.isUnlocked) {
                val shouldUnlock = when (achievement.id) {
                    "levels_10" -> stats.totalLevelsCompleted >= 10
                    "levels_50" -> stats.totalLevelsCompleted >= 50
                    "moves_1000" -> stats.totalMovesMade >= 1000
                    "moves_5000" -> stats.totalMovesMade >= 5000
                    "coins_1000" -> stats.totalCoinsCollected >= 1000
                    "streak_7" -> stats.loginStreak >= 7
                    else -> false
                }

                if (shouldUnlock) {
                    val updated = achievement.copy(
                        isUnlocked = true,
                        unlockedTimestamp = System.currentTimeMillis()
                    )
                    repository.updateAchievement(updated)
                    newlyUnlocked.add(updated)

                    // Award coins
                    val currentCoins = repository.getCoinBalance().first()
                    repository.updateCoinBalance(currentCoins + achievement.rewardCoins)
                }
            }
        }
        return newlyUnlocked
    }
}
