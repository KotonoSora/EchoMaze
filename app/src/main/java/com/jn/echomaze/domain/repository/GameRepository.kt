package com.jn.echomaze.domain.repository

import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun updateStats(stats: UserStats)
    
    fun getHistory(): Flow<List<GameHistory>>
    suspend fun insertHistory(history: GameHistory)
    
    suspend fun resetGame()
}
