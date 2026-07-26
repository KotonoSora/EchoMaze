package com.jn.echomaze.domain.repository

import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.Level
import com.jn.echomaze.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllLevels(): Flow<List<Level>>
    suspend fun getLevelById(levelId: Int): Level?
    suspend fun updateLevel(level: Level)
    suspend fun insertLevels(levels: List<Level>)

    fun getCoinBalance(): Flow<Int>
    suspend fun updateCoinBalance(balance: Int)

    fun getGameStats(): Flow<UserStats?>
    suspend fun updateStats(stats: UserStats)

    fun getAllHistory(): Flow<List<GameHistory>>
    fun getTopScores(): Flow<List<GameHistory>>
    suspend fun insertHistory(history: GameHistory)

    fun getAllAchievements(): Flow<List<Achievement>>
    suspend fun updateAchievement(achievement: Achievement)
    suspend fun insertAchievements(achievements: List<Achievement>)
}
