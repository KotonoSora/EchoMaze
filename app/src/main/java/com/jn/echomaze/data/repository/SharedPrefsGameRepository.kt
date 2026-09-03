package com.jn.echomaze.data.repository

import com.jn.echomaze.data.PreferenceManager
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class SharedPrefsGameRepository(
    private val preferenceManager: PreferenceManager
) : GameRepository {
    override fun getUserStats(): Flow<UserStats> = preferenceManager.getUserStatsFlow()

    override suspend fun updateStats(stats: UserStats) {
        preferenceManager.updateStats(stats)
    }

    override fun getHistory(): Flow<List<GameHistory>> = preferenceManager.getHistoryFlow()

    override suspend fun insertHistory(history: GameHistory) {
        val currentHistory = preferenceManager.getHistory().toMutableList()
        currentHistory.add(0, history)
        // Keep only last 50 entries
        val updatedHistory =
            if (currentHistory.size > 50) currentHistory.take(50) else currentHistory
        preferenceManager.saveHistory(updatedHistory)
    }

    override suspend fun resetGame() {
        preferenceManager.reset()
    }
}
