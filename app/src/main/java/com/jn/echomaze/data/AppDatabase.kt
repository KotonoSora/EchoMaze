package com.jn.echomaze.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [LevelEntity::class, CoinEntity::class, StatsEntity::class, GameHistoryEntity::class, AchievementEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "echo_maze_database"
                )
                    .addCallback(object : Callback() {
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                // Seed if empty
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.gameDao()
                    if (dao.getLevelCount() == 0) {
                        seedDatabase(dao)
                    }
                }

                instance
            }
        }

        private suspend fun seedDatabase(dao: GameDao) {
            // Initial levels
            dao.insertLevels(
                listOf(
                    LevelEntity(id = 1, levelNumber = 1, isUnlocked = true),
                    LevelEntity(id = 2, levelNumber = 2, isUnlocked = false),
                    LevelEntity(id = 3, levelNumber = 3, isUnlocked = false),
                    LevelEntity(id = 4, levelNumber = 4, isUnlocked = false),
                    LevelEntity(id = 5, levelNumber = 5, isUnlocked = false),
                    LevelEntity(id = 6, levelNumber = 6, isUnlocked = false),
                    LevelEntity(id = 7, levelNumber = 7, isUnlocked = false),
                    LevelEntity(id = 8, levelNumber = 8, isUnlocked = false),
                    LevelEntity(id = 9, levelNumber = 9, isUnlocked = false)
                )
            )
            // Initial coins
            dao.updateCoinBalance(CoinEntity(balance = 100))
            // Initial stats
            dao.updateStats(StatsEntity())
            // Initial Achievements
            dao.insertAchievements(
                listOf(
                    AchievementEntity(
                        "levels_10",
                        "PRO SLIDER",
                        "Complete 10 levels.",
                        rewardCoins = 200
                    ),
                    AchievementEntity(
                        "levels_50",
                        "PUZZLE MASTER",
                        "Complete 50 levels.",
                        rewardCoins = 1000
                    ),
                    AchievementEntity(
                        "moves_1000",
                        "EFFICIENT",
                        "Make 1000 total moves.",
                        rewardCoins = 150
                    ),
                    AchievementEntity(
                        "moves_5000",
                        "STAMINA",
                        "Make 5000 total moves.",
                        rewardCoins = 500
                    ),
                    AchievementEntity(
                        "coins_1000",
                        "RICHES",
                        "Collect 1000 coins.",
                        rewardCoins = 300
                    ),
                    AchievementEntity(
                        "streak_7",
                        "LOYALIST",
                        "7 day login streak.",
                        rewardCoins = 500
                    )
                )
            )
        }
    }
}
