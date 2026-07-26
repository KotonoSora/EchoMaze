package com.jn.echomaze.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    // Level related
    @Query("SELECT COUNT(*) FROM levels")
    suspend fun getLevelCount(): Int

    @Query("SELECT * FROM levels ORDER BY levelNumber ASC")
    fun getAllLevels(): Flow<List<LevelEntity>>

    @Query("SELECT * FROM levels WHERE id = :levelId")
    suspend fun getLevelById(levelId: Int): LevelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<LevelEntity>)

    @Update
    suspend fun updateLevel(level: LevelEntity)

    // Coin related
    @Query("SELECT * FROM coin_balance WHERE id = 1")
    fun getCoinBalance(): Flow<CoinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCoinBalance(coinEntity: CoinEntity)

    // Stats related
    @Query("SELECT * FROM game_stats WHERE id = 1")
    fun getGameStats(): Flow<StatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStats(stats: StatsEntity)

    // History related
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: GameHistoryEntity)

    @Query("SELECT * FROM game_history ORDER BY timestamp DESC LIMIT 50")
    fun getAllHistory(): Flow<List<GameHistoryEntity>>

    @Query("SELECT * FROM game_history ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<GameHistoryEntity>>

    // Achievement related
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)
}
