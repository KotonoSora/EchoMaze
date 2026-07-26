package com.jn.echomaze.data.repository

import com.jn.echomaze.data.CoinEntity
import com.jn.echomaze.data.GameDao
import com.jn.echomaze.data.mapper.toDomain
import com.jn.echomaze.data.mapper.toEntity
import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.Level
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineGameRepository(private val gameDao: GameDao) : GameRepository {
    override fun getAllLevels(): Flow<List<Level>> =
        gameDao.getAllLevels().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getLevelById(levelId: Int): Level? =
        gameDao.getLevelById(levelId)?.toDomain()

    override suspend fun updateLevel(level: Level) =
        gameDao.updateLevel(level.toEntity())

    override suspend fun insertLevels(levels: List<Level>) =
        gameDao.insertLevels(levels.map { it.toEntity() })

    override fun getCoinBalance(): Flow<Int> =
        gameDao.getCoinBalance().map { it?.balance ?: 0 }

    override suspend fun updateCoinBalance(balance: Int) =
        gameDao.updateCoinBalance(CoinEntity(balance = balance))

    override fun getGameStats(): Flow<UserStats?> =
        gameDao.getGameStats().map { it?.toDomain() }

    override suspend fun updateStats(stats: UserStats) =
        gameDao.updateStats(stats.toEntity())

    override fun getAllHistory(): Flow<List<GameHistory>> =
        gameDao.getAllHistory().map { entities -> entities.map { it.toDomain() } }

    override fun getTopScores(): Flow<List<GameHistory>> =
        gameDao.getTopScores().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insertHistory(history: GameHistory) =
        gameDao.insertHistory(history.toEntity())

    override fun getAllAchievements(): Flow<List<Achievement>> =
        gameDao.getAllAchievements().map { entities -> entities.map { it.toDomain() } }

    override suspend fun updateAchievement(achievement: Achievement) =
        gameDao.updateAchievement(achievement.toEntity())

    override suspend fun insertAchievements(achievements: List<Achievement>) =
        gameDao.insertAchievements(achievements.map { it.toEntity() })
}
